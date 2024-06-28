package com.glide.qa.backend.common.util;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

import com.github.javafaker.Faker;
import com.glide.qa.backend.admin.data.TestData;
import com.glide.qa.config.logging.CustomLogFilter;
import com.google.gson.Gson;
import io.restassured.filter.Filter;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Locale;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is util class where we have some common methods.
 *
 * @author sujitpandey
 *
 */
public class Util {

  private Util() {

  }

  public static final Gson GSON = new Gson();
  public static final Filter LOGFILTER = new CustomLogFilter();
  public static final Faker FAKER = new Faker(Locale.FRANCE);

  /**
   * This method compares two date and return result as boolean.
   *
   * @param firstDate - Should be String
   * @param secondDate - Should be String
   * @return Boolean
   */
  public static Boolean isSameDate(String firstDate, String secondDate) {
    LocalDateTime first = LocalDateTime.parse(firstDate, ISO_OFFSET_DATE_TIME);
    LocalDateTime second = LocalDateTime.parse(secondDate, ISO_OFFSET_DATE_TIME);

    long diff = ChronoUnit.SECONDS.between(first, second);
    return diff < 5;
  }



  /**
   * This common method reduces the given hours from given date. The given datetime may be with given zone id or without
   * it.
   *
   * <pre>
   *  it will handle
   *1. with zone id:
   * {@code 2021-03-23T10:43:32.010069453+02:00
   * 2021-03-23T10:43:32+02:00
   * 2021-03-23T10:43+02:00
   *}
   *with reducing 1 hour will result
   *{@code 2021-03-23T09:43+02:00
   *2021-03-23T09:43+02:00
   *2021-03-23T09:43+02:00
   *}
   * </pre>
   *
   * <pre>
   *  and also without zone id:
   * {@code 2021-03-23T10:43:32.010069453
   * 2021-03-23T10:43:32
   * 2021-03-23T10:43}
   *
   *with reducing 1 hour will result
   *{@code 2021-03-23T09:43
   *2021-03-23T09:43
   *2021-03-23T09:43
   *}
   * </pre>
   *
   * @param date : Should be String
   * @param hours : Should be {@link int}
   * @return String
   */
  public static String reduceHours(String date, int hours) {
    LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
    String newValue = "";
    String[] data1 = date.split("\\+");
    LocalDateTime datam = dateTime.truncatedTo(ChronoUnit.MINUTES).minusHours(hours);
    if (data1.length == 2) {
      newValue = datam.toString() + "+" + data1[1];
    } else {
      newValue = datam.toString();
    }
    return newValue;
  }

  /**
   * This common method extends the given hours from given date.
   *
   * <pre>
   *  it will handle
   *1. with zone id:
   * {@code 2021-03-23T10:43:32.010069453+02:00
   * 2021-03-23T10:43:32+02:00
   * 2021-03-23T10:43+02:00
   *}
   *with adding 1 hour will result
   *{@code 2021-03-23T11:43+02:00
   *2021-03-23T11:43+02:00
   *2021-03-23T11:43+02:00
   *}
   * </pre>
   *
   * <pre>
   *  and also without zone id:
   * {@code 2021-03-23T10:43:32.010069453
   * 2021-03-23T10:43:32
   * 2021-03-23T10:43}
   *
   *with adding 1 hour will result
   *{@code 2021-03-23T11:43
   *2021-03-23T11:43
   *2021-03-23T11:43
   *}
   * </pre>
   *
   * @param date : Should be String
   * @param hours minutes : Should be {@link int}
   * @return String
   */
  public static String extendHours(String date, int hours) {
    LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
    String newValue = "";
    String[] data1 = date.split("\\+");

    LocalDateTime datam = dateTime.truncatedTo(ChronoUnit.MINUTES).plusHours(hours);
    if (data1.length == 2) {
      newValue = datam.toString() + "+" + data1[1];
    } else {
      newValue = datam.toString();
    }

    return newValue;
  }

  /**
   * This common method reduces the given minutes from given date.
   *
   * <pre>
   *  it will handle
   *1. with zone id:
   * {@code 2021-03-23T10:43:32.010069453+02:00
   * 2021-03-23T10:43:32+02:00
   * 2021-03-23T10:43+02:00
   *}
   *with reducing 10 mins will result
   *{@code 2021-03-23T10:33+02:00
   *2021-03-23T10:33+02:00
   *2021-03-23T10:33+02:00
   *}
   * </pre>
   *
   * <pre>
   *  and also without zone id:
   * {@code 2021-03-23T10:43:32.010069453
   * 2021-03-23T10:43:32
   * 2021-03-23T10:43}
   *
   *with adding 10 mins will result
   *{@code 2021-03-23T10:33
   *2021-03-23T10:33
   *2021-03-23T10:33
   *}
   * </pre>
   *
   * @param date : Should be String
   * @param minutes : Should be {@link int}
   * @return String
   */
  public static String reduceMinutes(String date, int minutes) {
    LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
    String newValue = "";
    String[] data1 = date.split("\\+");
    LocalDateTime datam = dateTime.truncatedTo(ChronoUnit.MINUTES).minusMinutes(minutes);
    if (data1.length == 2) {
      newValue = datam.toString() + "+" + data1[1];
    } else {
      newValue = datam.toString();
    }
    return newValue;
  }

  /**
   * This common method extends the given minutes from given date.
   *
   * <pre>
   *  it will handle
   *1. with zone id:
   * {@code 2021-03-23T10:43:32.010069453+02:00
   * 2021-03-23T10:43:32+02:00
   * 2021-03-23T10:43+02:00
   *}
   *with adding 10 mins will result
   *{@code 2021-03-23T10:53+02:00
   *2021-03-23T10:53+02:00
   *2021-03-23T10:53+02:00
   *}
   * </pre>
   *
   * <pre>
   *  and also without zone id:
   * {@code 2021-03-23T10:43:32.010069453
   * 2021-03-23T10:43:32
   * 2021-03-23T10:43}
   *
   *with adding 10 mins will result
   *{@code 2021-03-23T10:53
   *2021-03-23T10:53
   *2021-03-23T10:53
   *}
   * </pre>
   *
   * @param date : Should be String
   *
   * @param minutes minutes : Should be {@link int}
   * @return String
   */
  public static String extendMinutes(String date, int minutes) {
    LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
    String newValue = "";
    String[] data1 = date.split("\\+");
    LocalDateTime datam = dateTime.truncatedTo(ChronoUnit.MINUTES).plusMinutes(minutes);
    if (data1.length == 2) {
      newValue = datam.toString() + "+" + data1[1];
    } else {
      newValue = datam.toString();
    }
    return newValue;
  }


  /**
   * This method will parse the give date and time to java local date and time. the pattern should list from longest to
   * shortest otherwise it won't work
   *
   * @param input : should be String
   * @return {@link LocalDateTime}
   */
  public static LocalDateTime dateTimeParser(String input) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(""
        + "[yyyy-MM-dd'T'HH:mm:ss.SSSSSS]"
        + "[yyyy-MM-dd'T'HH:mm:ss[.SSS][XXX]]"
        + "[yyyy-MM-dd'T'HH:mm:ss[XXX]]"
        + "[yyyy/MM/dd HH:mm:ss.SSSSSS]"
        + "[yyyy-MM-dd HH:mm:ss[.SSS]]"
        + "[yyyy-MM-dd'T'HH:mm[XXX]]"
        + "[dd/MM/yyyy HH:mm]"
        + "[yyyy-MM-dd'T'HH:mm:ss]",
        Locale.ENGLISH);

    return LocalDateTime.parse(input, formatter);
  }

  /**
   * This method will parse the give date and time to java local date and time. the pattern should list from longest to
   * shortest otherwise it won't work
   *
   * @param input : should be LocalDateTime
   * @return {@link String}
   */
  public static String dateTimeParser(LocalDateTime input) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(""
        + "[yyyy-MM-dd'T'HH:mm:ss]",
        Locale.ENGLISH);

    return input.format(formatter);
  }

  /**
   * This method will parse the give date and time to java local date and time. the pattern should list from longest to
   * shortest otherwise it won't work
   *
   * @param input : should be String
   * @return {@link LocalDateTime}
   */
  public static LocalDateTime stringToLocalDateTime(String input) {

    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        .optionalStart()
        .appendZoneOrOffsetId()
        .optionalEnd()
        .toFormatter();

    return LocalDateTime.parse(input, formatter);

  }



  /**
   * This method return only date from java local date and time.
   *
   * @param dateTime : should be String
   * @return {@link LocalDate}
   */
  public static LocalDate returnDateOnly(String dateTime) {
    return LocalDate.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME);

  }

  /**
   * This will parse the Date and return java local date.
   *
   * @param input : should be String
   * @return {@link LocalDate}
   */
  public static LocalDate dateParser(String input) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(""
        + "[yyyy/MM/dd]"
        + "[yyyy-MM-dd]"
        + "[dd/MM/yyyy]"
        + "[ddMMMyyyy]", Locale.ENGLISH);

    return LocalDate.parse(input, formatter);
  }

  /**
   * This method subtracts two localdate and return the results in seconds.
   *
   * @return long
   * 
   */
  public static long dateAndTimeSubstractor(LocalDateTime from, LocalDateTime to) {
    return Duration.between(from, to).getSeconds();

  }


  /**
   * This method add sleep in each method when called.
   *
   * @param millis - Should be long
   */
  public static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
  }



  /**
   * This static method will extract the jwt token and return the total validity time in hours.
   *
   * @param token - Should be String
   * @return - int
   */
  public static int jwtTokenDecoder(String token) {

    Base64.Decoder decoder = Base64.getUrlDecoder();
    String[] chunks = token.split("\\.");
    String payload = new String(decoder.decode(chunks[1]));
    JSONObject testV = null;
    int startTime = 0;
    int endTime = 0;
    try {
      testV = new JSONObject(payload);
      startTime = Integer.parseInt(testV.get("iat").toString());
      endTime = Integer.parseInt(testV.get("exp").toString());
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return (endTime - startTime) / 3600;
  }

  /**
   * This method decodes the urls.
   *
   * @param url Should be encoded url
   * @return String
   */
  public static String decodeUrl(String url) {
    try {
      return java.net.URLDecoder.decode(url, StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      return null;
    }
  }

  /**
   * This will returns the file mime type.
   *
   * @param file Should be String
   * @return String
   */
  public static String getMimeType(String file) {
    try {

      FileNameMap fileNameMap = URLConnection.getFileNameMap();
      return fileNameMap.getContentTypeFor(file);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Get absolute file path as String of a given location.
   *
   * @param filename Should be String
   * @return String
   */
  public static String getAbsolutePathOfFile(String filename) {
    return Paths.get(filename).toString();
  }

  /**
   * This methods convers the file into string content.
   *
   * @param fileName Should be String
   * @return String
   */
  public static String fileToStringContent(String fileName) {
    try {
      return FileUtils.readFileToString(new File(fileName));

    } catch (Exception e) {
      return null;
    }

  }


}
