package com.glide.qa.backend.common.util;

import com.github.javafaker.Faker;
import java.time.LocalDateTime;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * This is random generator class for different kind of random values can be generated.
 *
 * @author sujitpandey
 *
 */
public class RandomGenerator {

  private RandomGenerator() {

  }

  private static final Random RANDOM = new Random();

  /**
   * This will generate random integer number from given lower and upper digit.
   *
   * @param firstLimit - Should be {@link Integer}
   * @param secondLimit - Should be {@link Integer}
   * @return - {@link Integer}
   */
  public static int randomInt(int firstLimit, int secondLimit) {
    return RANDOM.nextInt((secondLimit - firstLimit) + 1) + firstLimit;
  }

  /**
   * This will generate the random integer of given length.
   *
   * @return - {@link Long}
   */
  public static long randomInt(int length) {
    return Math.round(Math.floor(Math.pow(10, length - (double) 1) * 6 + Math.random() * 900000));
  }

  /**
   * This will generate random integer with with next 100 random with adding one.
   *
   * @return - {@link Integer}
   */
  public static int randomInt() {
    return RANDOM.nextInt(100) + 1;
  }

  /**
   * This will generate the random double value with limit of given min and max double value.
   *
   * @param min - Should be {@link Double}
   * @param max - Should be {@link Double}
   * @return - {@link Double}
   */
  public static double randomDouble(double min, double max) {
    return RANDOM.nextDouble() * (max - min) + min;
  }

  /**
   * This will generate the random double value.
   *
   * @return - Double
   */
  public static boolean randomBoolean() {
    return RANDOM.nextBoolean();
  }

  /**
   * This will generate the random alphanumeric value of fix length of six.
   *
   * @return - {@link String}
   */
  public static String randomAlphanumeric() {
    return RandomStringUtils.randomAlphanumeric(6);
  }

  /**
   * This will generate the random alphanumeric value with given length of integer.
   *
   * @param length - Should be {@link Integer}
   * @return - {@link String}
   */
  public static String randomAlphanumeric(int length) {
    return RandomStringUtils.randomAlphanumeric(length);
  }

  /**
   * This will generate the random alphabet of fix length of 6.
   *
   * @return - {@link String}
   */
  public static String randomAlphabetic() {
    return RandomStringUtils.randomAlphabetic(6);
  }

  /**
   * This will generate the random alphabet of given length.
   *
   * @param length - Should be Integer
   * @return - {@link String}
   */
  public static String randomAlphabetic(int length) {
    return RandomStringUtils.randomAlphabetic(length);
  }


  /**
   * This will generate the random name of fix length of 6.
   *
   * @return - {@link String}
   */
  public static String randomName() {
    String data = RandomStringUtils.randomAlphabetic(6);
    return data.substring(0, 1).toUpperCase() + data.substring(1).toLowerCase();
  }

  /**
   * This will generate the random name of fix length of given length.
   *
   * @param length - should be Integer
   * @return - {@link String}
   */
  public static String randomName(int length) {
    String data = RandomStringUtils.randomAlphabetic(length);
    return data.substring(0, 1).toUpperCase() + data.substring(1).toLowerCase();
  }

  /**
   * This static method generates the random email for @glide-mobility.com with starting with. abc
   *
   * @return - {@link String}
   */
  public static String randomEmail() {
    return "abc+" + randomAlphanumeric().toLowerCase() + "@glide-mobility.com";

  }

  /**
   * This will generate the random enum from the given enum class.
   *
   * @param <T> - should be {@link Enum}
   * @param clazz - should be {@link Enum} class
   * @return - {@link Enum} value
   */
  public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
    int x = RANDOM.nextInt(clazz.getEnumConstants().length);
    return clazz.getEnumConstants()[x];
  }

  /**
   * This will return the current date and time as String.
   *
   * @return - {@link String}
   */
  public static String getCurrentDate() {
    return LocalDateTime.now() + "Z";
  }

  /**
   * This will return the date and time with added values in current date and time as String.
   *
   * @param month - Should be {@link Integer}
   * @param days - Should be {@link Integer}
   * @param hours - Should be {@link Integer}
   * @param min - Should be {@link Integer}
   * @param sec - Should be {@link Integer}
   * @return - {@link String}
   */
  public static String getCurrentDatePlusPeriod(int month, int days, int hours, int min, int sec) {
    return LocalDateTime.now().plusDays(days).plusHours(hours).plusMinutes(min).plusSeconds(sec).plusMonths(month)
        + "Z";
  }

  public static String randomImage() {
    return Faker.instance().internet().image();
  }

  public static String randomUrl() {
    return Faker.instance().internet().url();
  }

  public static String randomHexColor() {
    return Faker.instance().color().hex(true);
  }



}
