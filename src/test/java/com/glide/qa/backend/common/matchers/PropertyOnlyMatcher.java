package com.glide.qa.backend.common.matchers;

import static org.hamcrest.beans.PropertyUtil.propertyDescriptorsFor;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.testng.annotations.Factory;

/**
 * This is the custom matcher for hamcrest to validate only the properties name in the given bean. It will be useful
 * when we try to validate our object with give properties.
 *
 * @author sujitpandey
 *
 * @param <T> Should be bean
 */

public class PropertyOnlyMatcher<T> extends TypeSafeDiagnosingMatcher<T> {
  private final T expectedBean;
  private final List<String> propertyNames;

  private PropertyOnlyMatcher(T expectedBean) {
    PropertyDescriptor[] descriptors = propertyDescriptorsFor(expectedBean, Object.class);
    this.expectedBean = expectedBean;
    this.propertyNames = Arrays.stream(descriptors).map(x -> x.getReadMethod().getName()).collect(Collectors.toList());
  }


  @Override
  public void describeTo(Description description) {
    description.appendText("same property as " + expectedBean.getClass().getSimpleName());

  }

  @Override
  public boolean matchesSafely(T bean, Description mismatch) {
    // hasMatchingValues(bean, mismatch);
    return isCompatibleType(bean, mismatch)
        && hasNoExtraProperties(bean, mismatch);
  }

  private boolean hasNoExtraProperties(T item, Description mismatch) {

    PropertyDescriptor[] descriptor = propertyDescriptorsFor(item, Object.class);
    List<String> getter = Arrays.stream(descriptor).map(x -> x.getReadMethod().getName()).collect(Collectors.toList());
    getter.removeAll(propertyNames);
    if (!getter.isEmpty()) {
      mismatch.appendText("has extra properties called " + getter);
      return false;
    }
    return true;
  }

  private boolean isCompatibleType(T item, Description mismatchDescription) {
    if (!expectedBean.getClass().isAssignableFrom(item.getClass())) {
      mismatchDescription.appendText("is incompatible type: " + item.getClass().getSimpleName());
      return false;
    }
    return true;
  }

  @Factory
  public static <T> Matcher<T> propertyMatcherOnly(T expectedBean) {
    return new PropertyOnlyMatcher<>(expectedBean);
  }


}
