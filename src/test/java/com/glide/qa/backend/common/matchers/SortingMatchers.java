package com.glide.qa.backend.common.matchers;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import com.glide.qa.backend.admin.data.enumeration.SortDirection;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * @author sujitpandey
 */
public class SortingMatchers {

  private SortingMatchers() {

  }

  /**
   * This Custom Matcher is used to check if a List of objects is correctly sorted by a field
   *
   * @param fieldSortedBy field by which you want to check if the List is sorted
   * @param sortDirection sort direction ASC or DESC which you expect your list to have
   * @return Matcher which analyzes if your list is sorted by one field in the sort direction expected
   */
  public static Matcher<List> isListSortedByField(String fieldSortedBy, SortDirection sortDirection) {
    return new TypeSafeMatcher<List>() {

      @Override
      public void describeTo(Description description) {
        description.appendText("List sorted by field: " + fieldSortedBy + " direction: " + sortDirection.toString());

      }

      @Override
      protected boolean matchesSafely(List listToBeChecked) {
        return checkSortingByString(listToBeChecked, fieldSortedBy, sortDirection);

      }

      @Override
      public void describeMismatchSafely(final List listToBeChecked, final Description mismatchDescription) {
        mismatchDescription.appendText("was list which is not sorted by: " + fieldSortedBy + " direction: " +
            sortDirection.toString());
      }


    };

  }

  public static Matcher<List> isListSortedByUUID(String fieldSortedBy, SortDirection sortDirection) {
    return new TypeSafeMatcher<List>() {

      @Override
      public void describeTo(Description description) {
        description.appendText("List sorted by field: " + fieldSortedBy + " direction: " + sortDirection.toString());

      }

      @Override
      protected boolean matchesSafely(List listToBeChecked) {
        return checkSortingByUUID(listToBeChecked, fieldSortedBy, sortDirection);
      }

      @Override
      protected void describeMismatchSafely(final List listToBeChecked, final Description mismatchDescription) {
        mismatchDescription.appendText("was list which is not sorted by: " + fieldSortedBy + " direction: " +
            sortDirection.toString());
      }

    };
  }

  /**
   * This method checks if a List of objects is sorted by one UUID field in the expected sort direction ( ASC / DESC )
   *
   * @param objects the list which you want to check if it is sorted
   * @param fieldSortedBy field by which you want to check if the list is sorted
   * @param sortDirection sort direction ASC or DESC which you expect your list to have
   * @return true or false depending if the list is sorted or not by the field in the sort direction expected
   */
  private static boolean checkSortingByUUID(List objects, String fieldSortedBy, SortDirection sortDirection) {
    boolean result = false;
    for (int i = 0; i < objects.size() - 1; i++) {
      try {
        Field field1 = objects.get(i).getClass().getDeclaredField(fieldSortedBy);
        Field field2 = objects.get(i + 1).getClass().getDeclaredField(fieldSortedBy);
        field1.setAccessible(true);
        field2.setAccessible(true);
        UUID value1 = (UUID) field1.get(objects.get(i));
        UUID value2 = (UUID) field2.get(objects.get(i + 1));
        if (sortDirection.equals(SortDirection.ASC) & value1.compareTo(value2) <= 0 ||
            sortDirection.equals(SortDirection.DESC) & value1.compareTo(value2) >= 0) {
          result = true;
        } else {
          return false;
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
      return result;
    }

    return false;

  }

  /**
   * This method checks if a List of objects is sorted by one String field in the expected sort direction ( ASC / DESC )
   *
   * @param objects the list which you want to check if it is sorted
   * @param fieldSortedBy field by which you want to check if the list is sorted
   * @param sortDirection sort direction ASC or DESC which you expect your list to have
   * @return true or false depending if the list is sorted or not by the field in the sort direction expected
   */
  private static boolean checkSortingByString(List objects, String fieldSortedBy, SortDirection sortDirection) {

    boolean result = false;
    try {
      for (int i = 0; i < objects.size() - 1; i++) {
        Field fieldOne = objects.get(i).getClass().getDeclaredField(fieldSortedBy);
        Field fieldTwo = objects.get(i + 1).getClass().getDeclaredField(fieldSortedBy);
        fieldOne.setAccessible(true);
        fieldTwo.setAccessible(true);
        String valueOne = (String) fieldOne.get(objects.get(i));

        String valueTwo = (String) fieldTwo.get(objects.get(i + 1));

        if (sortDirection.equals(SortDirection.ASC) & valueOne.compareToIgnoreCase(valueTwo) <= 0 ||
            sortDirection.equals(SortDirection.DESC) & valueOne.compareToIgnoreCase(valueTwo) >= 0) {
          result = true;
        } else {
          return false;
        }

      }
    } catch (Exception exception) {
    }
    return result;
  }



}
