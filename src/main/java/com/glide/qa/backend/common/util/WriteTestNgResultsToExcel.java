package com.glide.qa.backend.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.PresetColor;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFShapeProperties;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.AxisCrosses;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.BarDirection;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.XDDFBarChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ITestResult;


/**
 * This class has all logic to write in testngResult into excel file.
 *
 * @author sujitpandey
 *
 */
public class WriteTestNgResultsToExcel {

  private WriteTestNgResultsToExcel() {

  }


  /**
   * This method will create the excel sheet from given testng result.
   *
   * @param results - Should be {@link List} of ITestResult
   */
  public static void runExclScript(List<ITestResult> results) {

    try (Workbook workbook = new XSSFWorkbook()) {

      Sheet sheet = workbook.createSheet("Test_Results");

      createHeader(workbook, sheet);
      // createGraphHeader(results);

      // Write the data to the sheet
      int rowNum = 1;
      for (ITestResult result : results) {
        Row row = sheet.createRow(rowNum++);

        Cell testClassNameCell = row.createCell(0);
        writeTestCaseName(testClassNameCell, result.getInstanceName());
        Cell testNameCell = row.createCell(1);
        writeTestMethodName(testNameCell, result.getName());
        Cell statusCell = row.createCell(2);

        Cell comment = row.createCell(3);
        Cell stackTrace = row.createCell(7);
        writeStatus(statusCell, result, workbook, comment, stackTrace);
        Cell startDate = row.createCell(4);
        writeStartDate(startDate, result.getStartMillis());
        Cell endDate = row.createCell(5);
        writeEndDate(endDate, result.getEndMillis());
        Cell totalTime = row.createCell(6);
        writeTotalTime(totalTime, result.getStartMillis(), result.getEndMillis());



      }

      sheet.autoSizeColumn(0);
      sheet.autoSizeColumn(1);


      // Write the workbook to a file

      File pathToFile = new File("target/test-reports/excelReport");
      pathToFile.mkdirs();

      String datetime =
          LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
      FileOutputStream outputStream =
          new FileOutputStream(pathToFile + "/testng_results_" + datetime + ".xlsx");
      workbook.write(outputStream);

      outputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }



  private static void writeTotalTime(Cell totalTime, long startMillis, long endMillis) {

    if (endMillis == 0 || startMillis == 0) {
      totalTime.setCellValue("0");
    } else {
      double data = (endMillis - (double) startMillis) / 1000;
      totalTime.setCellValue(String.valueOf(data));
    }

  }


  private static void writeStartDate(Cell startDate, long startMillis) {
    startDate.setCellValue(startMillis != 0 ? String.valueOf(startMillis) : "0");

  }

  private static void writeEndDate(Cell endDate, long endMillis) {
    endDate.setCellValue(endMillis != 0 ? String.valueOf(endMillis) : "0");

  }


  private static void setStackTrace(Cell stackTrace, Throwable throwable, Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    style.setWrapText(true);
    stackTrace.setCellValue(throwable != null ? throwable.getStackTrace()[1].toString().substring(0, 100) : "");

  }

  private static void setComment(Cell comment, String message) {
    comment.setCellValue(message != null ? message.substring(0, 100) : "");

  }

  private static void writeStatus(Cell statusCell, ITestResult result, Workbook workbook, Cell comment,
      Cell stackTrace) {
    CellStyle style = workbook.createCellStyle();

    if (result.getStatus() == 1) {
      statusCell.setCellValue("PASS");
    } else if (result.getStatus() == 2) {
      statusCell.setCellValue("FAIL");
    } else {
      statusCell.setCellValue("SKIP");
    }


    switch (result.getStatus()) {

      case ITestResult.SUCCESS:

        style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        statusCell.setCellStyle(style);
        break;

      case ITestResult.FAILURE:

        style.setFillForegroundColor(IndexedColors.RED.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        statusCell.setCellStyle(style);
        // Add the comment if the result is a failure
        // setComment(comment, result.getThrowable().getMessage());

        // add stack trace here
        // setStackTrace(stackTrace, result.getThrowable(), workbook);
        break;

      case ITestResult.SKIP:
        style.setFillForegroundColor(IndexedColors.DARK_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        statusCell.setCellStyle(style);
        // Add the comment if the result is a failure
        // setComment(comment, result.getThrowable().getMessage());

        // add stack trace here
        // setStackTrace(stackTrace, result.getThrowable(), workbook);
        break;

      default:

    }


  }

  private static void writeTestMethodName(Cell testNameCell, String name) {
    testNameCell.setCellValue(name != null ? name : "");
  }

  private static void writeTestCaseName(Cell testClassNameCell, String instanceName) {
    testClassNameCell.setCellValue(instanceName != null ? instanceName : "");
  }

  private static void createHeader(Workbook workbook, Sheet sheet) {
    CellStyle headerStyle = workbook.createCellStyle();

    headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    Font headerFont = workbook.createFont();
    headerFont.setColor(IndexedColors.WHITE.getIndex());
    headerFont.setBold(true);
    headerStyle.setFont(headerFont);
    headerStyle.setBorderBottom(BorderStyle.DOUBLE);
    headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    headerStyle.setBorderLeft(BorderStyle.DOUBLE);
    headerStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    headerStyle.setBorderRight(BorderStyle.DOUBLE);
    headerStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
    headerStyle.setBorderTop(BorderStyle.DOUBLE);
    headerStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());



    String[] headers = {"Test Class Name", "Test Method Name", "Status", "Comment", "start Date in MS",
        "End Date in MS", "Total Time taken in Second", "Stack Trace"};
    // Create the header row
    Row headerRow = sheet.createRow(0);
    for (int i = 0; i < headers.length; i++) {
      Cell headerCell = headerRow.createCell(i);
      headerCell.setCellValue(headers[i]);
      headerCell.setCellStyle(headerStyle);
    }

  }

  /**
   * This will create the graphics for test results.
   *
   * @param results ITestResult
   */
  public static void createGraphHeader(List<ITestResult> results) {

    Map<String, Map<String, Integer>> groupedResults = new HashMap<>();

    for (ITestResult result : results) {
      String className = result.getTestClass().getName();
      int status = result.getStatus();
      String statusString = getStatusString(status);

      Map<String, Integer> classResults = groupedResults.get(className);
      if (classResults == null) {
        classResults = new HashMap<>();
        classResults.put("PASS", 0);
        classResults.put("FAIL", 0);
        classResults.put("SKIP", 0);
        groupedResults.put(className, classResults);
      }

      classResults.put(statusString, classResults.get(statusString) + 1);
    }


    try (Workbook workbook = new XSSFWorkbook()) {

      Sheet sheet = workbook.createSheet("Test_Results_with_graph");
      Row headerRow = sheet.createRow(0);
      headerRow.createCell(0).setCellValue("status");
      Row passRow = sheet.createRow(1);
      Row failRow = sheet.createRow(2);
      Row skipRow = sheet.createRow(3);
      passRow.createCell(0).setCellValue("PASS");
      failRow.createCell(0).setCellValue("FAIL");
      skipRow.createCell(0).setCellValue("SKIP");

      int rowNum = 1;
      for (Entry<String, Map<String, Integer>> data : groupedResults.entrySet()) {
        headerRow.createCell(rowNum).setCellValue(data.getKey());
        passRow.createCell(rowNum).setCellValue(data.getValue().get("PASS"));
        failRow.createCell(rowNum).setCellValue(data.getValue().get("FAIL"));
        skipRow.createCell(rowNum).setCellValue(data.getValue().get("SKIP"));
        rowNum++;
      }



      XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
      XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 4, 7, 20);

      XSSFChart chart = drawing.createChart(anchor);
      chart.setTitleText("Area-wise Top Seven Countries");
      chart.setTitleOverlay(false);

      XDDFChartLegend legend = chart.getOrAddLegend();
      legend.setPosition(LegendPosition.TOP_RIGHT);

      XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
      bottomAxis.setTitle("Country");
      XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
      leftAxis.setTitle("Area");
      leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
      int noOfColumns = sheet.getRow(0).getLastCellNum();


      XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromStringCellRange((XSSFSheet) sheet,
          new CellRangeAddress(0, 0, 1, noOfColumns - 1));

      XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange((XSSFSheet) sheet,
          new CellRangeAddress(1, 1, 1, noOfColumns - 1));
      XDDFNumericalDataSource<Double> values2 = XDDFDataSourcesFactory.fromNumericCellRange((XSSFSheet) sheet,
          new CellRangeAddress(2, 2, 1, noOfColumns - 1));
      XDDFNumericalDataSource<Double> values3 = XDDFDataSourcesFactory.fromNumericCellRange((XSSFSheet) sheet,
          new CellRangeAddress(3, 3, 1, noOfColumns - 1));

      XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
      XDDFChartData.Series series1 = data.addSeries(countries, values);
      XDDFChartData.Series series2 = data.addSeries(countries, values2);
      XDDFChartData.Series series3 = data.addSeries(countries, values3);
      series1.setTitle("pass", null);
      series2.setTitle("fail", null);
      series3.setTitle("skip", null);
      data.setVaryColors(true);
      chart.plot(data);

      // in order to transform a bar chart into a column chart, you just need to change the bar direction
      XDDFBarChartData bar = (XDDFBarChartData) data;

      bar.setBarDirection(BarDirection.COL);



      FileOutputStream fileout = new FileOutputStream("piechart.xlsx");
      workbook.write(fileout);
      fileout.close();

    } catch (Exception e) {
      e.printStackTrace();
    }


  }

  @SuppressWarnings("unused")
  private static void solidFillSeries(XDDFChartData data, int index, PresetColor color) {
    XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(color));
    @SuppressWarnings("deprecation")
    XDDFChartData.Series series = data.getSeries().get(index);
    XDDFShapeProperties properties = series.getShapeProperties();
    if (properties == null) {
      properties = new XDDFShapeProperties();
    }
    properties.setFillProperties(fill);
    series.setShapeProperties(properties);
  }

  private static String getStatusString(int status) {
    switch (status) {
      case 1:
        return "PASS";
      case 2:
        return "FAIL";
      case 3:
        return "SKIP";
      default:
        throw new IllegalArgumentException("Invalid status: " + status);
    }
  }



}

