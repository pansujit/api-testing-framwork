package com.glide.qa.backend.common.matchers;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * @author sujitpandey
 */
public class SortingMatcher {

  public static TypeSafeDiagnosingMatcher<List> sortListByString(String property, String direction) {
    return new TypeSafeDiagnosingMatcher<List>() {

      @Override
      public void describeTo(Description description) {
        description.appendText("List sorted by field: " + property + " direction: " + direction);

      }


      @Override
      protected boolean matchesSafely(List item, Description mismatchDescription) {
        return sortByString(item, direction, property);
      }

    };

  }



  private static boolean sortByString(List objects, String direction, String property) {
    boolean result = false;

    @SuppressWarnings("unchecked")
    List<String> data = (List<String>) objects.stream()
        .map(field -> {
          Field field1 = null;
          try {
            field1 = field.getClass().getDeclaredField(property);
          } catch (Exception e) {
            e.printStackTrace();
          }
          field1.setAccessible(true);
          try {
            return field1.get(field);
          } catch (Exception e) {
            e.printStackTrace();
          }
          return field1;
        })
        .collect(Collectors.toList());
    if (direction.equalsIgnoreCase("ASC")) {
      List<String> response = data.stream().sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList());

      result = response.equals(data);

      return result;

    } else if (direction.equalsIgnoreCase("DESC")) {
      List<String> response = data.stream().sorted(String.CASE_INSENSITIVE_ORDER.reversed())
          .collect(Collectors.toList());

      result = response.equals(data);
      return result;
    } else {
      return result;
    }

  }



}
