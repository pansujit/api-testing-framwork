package com.glide.qa.config.logging;

import com.glide.qa.backend.common.util.Util;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * This class retry the failed test.
 *
 * @author sujitpandey
 *
 */
public class RetryFailedTestAnalyzer implements IRetryAnalyzer {
  int failCount = 0;
  int maxRetry = 1;

  @Override
  public boolean retry(ITestResult result) {

    if (failCount < maxRetry) {
      Util.sleep(2000);
      failCount++;
      return true;

    }

    return false;
  }

}
