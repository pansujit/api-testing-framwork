package com.glide.qa.backend.common.matchers;

import com.glide.qa.backend.admin.data.enumeration.SortDirection;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is sorter class which sort the given list in given direction.
 *
 * @author sujitpandey
 *
 */
public class SortByField {

  private SortByField() {

  }

  /**
   * This is method to sort the list of type string in given order.
   *
   * @param response Should be List&lt;String&gt;
   * @param sortDirection Should be SortDirection
   * @return List&lt;String&gt;
   */
  public static List<String> sortByString(List<String> response, SortDirection sortDirection) {
    List<String> expectedList;

    if (sortDirection == SortDirection.ASC) {
      expectedList = response.stream()
          .sorted(String.CASE_INSENSITIVE_ORDER)
          .collect(Collectors.toList());
    } else if (sortDirection == SortDirection.DESC) {
      expectedList = response.stream()
          .sorted(String.CASE_INSENSITIVE_ORDER.reversed())
          .collect(Collectors.toList());
    } else {
      expectedList = null;
    }

    return expectedList;
  }


  /**
   * This is method to sort the list of type string in given order.
   *
   * @param response Should be List&lt;String&gt;
   * @param sortDirection Should be SortDirection
   * @return List&lt;String&gt;
   */
  public static List<String> sortByDateTime(List<String> response, SortDirection sortDirection) {
    List<String> expectedList;

    if (sortDirection == SortDirection.ASC) {
      expectedList = response.stream()
          .map(s -> ZonedDateTime.parse(s))
          .sorted()
          .map(p -> p.toString())
          .collect(Collectors.toList());
    } else if (sortDirection == SortDirection.DESC) {
      expectedList = response.stream()
          .map(s -> ZonedDateTime.parse(s))
          .sorted(Comparator.reverseOrder())
          .map(p -> p.toString())
          .collect(Collectors.toList());
    } else {
      expectedList = null;
    }

    return expectedList;
  }


}
