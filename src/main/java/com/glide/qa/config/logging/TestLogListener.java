package com.glide.qa.config.logging;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import com.glide.qa.backend.common.util.DeleteMail;
import com.glide.qa.backend.common.util.EmailSender;
import com.glide.qa.backend.common.util.Util;
import com.glide.qa.backend.common.util.WriteTestNgResultsToExcel;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.xml.XmlSuite;

/**
 * This is the listener class for tests.
 *
 * @author sujitpandey
 *
 */
@Log4j2
public class TestLogListener extends TestListenerAdapter implements ITestListener, ISuiteListener, IReporter {

  private static ExtentReports extent = new ExtentReports();
  private static final String SANITY = "sanity";
  private List<ITestResult> testResults = new ArrayList<>();


  private ExtentSparkReporter reporter =
      new ExtentSparkReporter(
          "target/test-reports/automationReport/report_" + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
              + ".html");


  @Override
  public void onTestStart(ITestResult result) {

    getConstantDelay();
    log.debug("**********************************");
    log.debug(returnClassNameWithMethod(result) + " started.");
    log.debug("**********************************");
  }


  @Override
  public void onTestSuccess(ITestResult result) {

    String testName = result.getInstanceName() + "." + result.getName();
    extentReporter(Status.PASS, testName, result);
    testResults.add(result);

    log.debug(returnClassNameWithMethod(result) + " is PASSED.");


  }

  @Override
  public void onTestFailure(ITestResult result) {

    String testName = result.getInstanceName() + "." + result.getName();
    testResults.add(result);
    extentReporter(Status.FAIL, testName, result);
    log.debug(returnClassNameWithMethod(result) + " is FAILED.");

  }

  @Override
  public void onTestSkipped(ITestResult result) {

    String testName = result.getInstanceName() + "." + result.getName();
    testResults.add(result);
    extentReporter(Status.SKIP, testName, result);
    log.debug(returnClassNameWithMethod(result) + " is SKIPPED.");

  }


  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    // auto generate method
  }

  /*
   * to add somthing
   */
  @Override
  public void onStart(ITestContext context) {
    /*
     * Nothing to add for the moment
     */
  }


  @Override
  public void onStart(ISuite suite) {
    try {
      //DeleteMail.cleanup();// add valid email and password to delete emails
      log.info("Delete emails have successfully completed.");
    } catch (Exception e) {
      log.error("delete email has encounter problem \n" + e.getMessage());
    }

    extent.setSystemInfo("ENV", System.getProperty("environment", "DEFAULT").toUpperCase());
    extent.setSystemInfo("OS", System.getProperty("os.name", "NOT_DETECTED").toUpperCase());
    extent.setSystemInfo("OS VERSION", System.getProperty("os.version", "NOT_FOUND").toUpperCase());
    extent.setSystemInfo("JAVA VERSION", System.getProperty("java.version", "NOT_AVAILABLE").toUpperCase());


    reporter.viewConfigurer().viewOrder().as(new ViewName[] {ViewName.DASHBOARD, ViewName.TEST,
        ViewName.EXCEPTION}).apply();
    reporter.config().setReportName("Sanity Test");

    reporter.config().setTimelineEnabled(true);
    reporter.config().setTheme(Theme.DARK);
    extent.attachReporter(reporter);



  }

  @Override
  public void onFinish(ITestContext context) {
    /*
     * nothing to do here
     */
  }

  @Override
  public void onFinish(ISuite suite) {
    List<ITestResult> testResults1 = new ArrayList<>();
    Map<String, ISuiteResult> getResult = suite.getResults();
    for (ISuiteResult isuiteResult : getResult.values()) {

      testResults1.addAll(
          isuiteResult.getTestContext().getPassedTests().getAllResults().stream().map(r -> r)
              .collect(Collectors.toList()));
      testResults1.addAll(
          isuiteResult.getTestContext().getFailedTests().getAllResults().stream().map(r -> r)
              .collect(Collectors.toList()));
      testResults1.addAll(
          isuiteResult.getTestContext().getSkippedTests().getAllResults().stream().map(r -> r)
              .collect(Collectors.toList()));
    }

    Collections.sort(testResults1);
    WriteTestNgResultsToExcel.runExclScript(testResults1);
    //EmailSender.emailSender(); // add useful username and password to send email

  }

  @Override
  public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
    // auto generate method

  }

  private String returnClassNameWithMethod(ITestResult result) {
    return "Test method " + result.getMethod().getMethodName();
  }


  private String getDuration(ITestResult result) {
    long duration = result.getEndMillis() - result.getStartMillis();

    return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(duration),
        TimeUnit.MILLISECONDS.toMinutes(duration) % TimeUnit.HOURS.toMinutes(1),
        TimeUnit.MILLISECONDS.toSeconds(duration) % TimeUnit.MINUTES.toSeconds(1));
  }

  private void getConstantDelay() {

    Util.sleep(1000);

  }

  /**
   * This is private method to add the test report in extent report.
   *
   * @param status - Should be Status
   * @param testname - Should be String
   * @param result - Should be ITestResult
   */
  private void extentReporter(Status status, String testname, ITestResult result) {

    extent.setReportUsesManualConfiguration(true);

    ExtentTest testData = extent.createTest(testname);
    testData.assignCategory(result.getInstanceName());
    testData.getModel().setStartTime(getTime(result.getStartMillis()));
    testData.getModel().setEndTime(getTime(result.getEndMillis()));

    testData.log(status, result.getThrowable())
        // Prints the test's description
        .info(result.getMethod().getDescription())
        // Prints the test's group
        .info(Arrays.toString(result.getMethod().getGroups()));
    extent.flush();

  }

  private Date getTime(long millis) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(millis);
    return calendar.getTime();
  }

  @Override
  public void beforeConfiguration(ITestResult tr) {
    super.beforeConfiguration(tr);
    if (!tr.getMethod().getMethodName().toLowerCase().contains("spring")) {
      log.info("=========== Configuration method '{}' started ===========", tr.getMethod().getMethodName());
    }
  }

  @Override
  public void onConfigurationSuccess(ITestResult tr) {
    super.onConfigurationSuccess(tr);
    if (!tr.getMethod().getMethodName().toLowerCase().contains("spring")) {
      log.info("=========== Configuration method '{}' finished ===========", tr.getMethod().getMethodName());
    }
  }

  @Override
  public void onConfigurationFailure(ITestResult tr) {
    if (!tr.getMethod().getMethodName().toLowerCase().contains("spring")) {
      super.onConfigurationFailure(tr);
      log.error("!!!!!!!!!!! Configuration method '{}' failed !!!!!!!!!!!", tr.getMethod().getMethodName());
      log.error(tr.getThrowable().getStackTrace());
    }
  }


  @Override
  public void onConfigurationSkip(ITestResult tr) {
    if (!tr.getMethod().getMethodName().toLowerCase().contains("spring")) {
      super.onConfigurationFailure(tr);
      log.error("!!!!!!!!!!! Configuration method '{}' Skipped !!!!!!!!!!!", tr.getMethod().getMethodName());
      log.error(tr.getThrowable().getStackTrace());
    }
  }



}
