package com.glide.qa.backend.common.matchers;

import static org.hamcrest.beans.PropertyUtil.NO_ARGUMENTS;
import static org.hamcrest.beans.PropertyUtil.propertyDescriptorsFor;
import static org.hamcrest.core.IsEqual.equalTo;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.testng.annotations.Factory;

/**
 * This is property matcher custom class.
 *
 * @author sujitpandey
 *
 * @param <T> - should be T
 */
public class PropertyValuesMatcher<T> extends TypeSafeDiagnosingMatcher<T> {
  private final T expectedBean;
  private final Set<String> propertyNames;
  private final List<PropertyValuesMatcher.PropertyMatcher> propertyMatchers;
  private final List<Matcher<? super T>> failed = new ArrayList<>();

  private PropertyValuesMatcher(T expectedBean) {

    PropertyDescriptor[] descriptors = propertyDescriptorsFor(expectedBean, Object.class);
    this.expectedBean = expectedBean;
    this.propertyNames = propertyNamesFrom(descriptors);
    this.propertyMatchers = propertyMatchersFor(expectedBean, descriptors);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("same property values as " + expectedBean.getClass().getSimpleName())
        .appendList(" [", ", ", "]", propertyMatchers);

  }

  @Override
  protected boolean matchesSafely(T bean, Description mismatch) {
    hasMatchingValues(bean, mismatch);
    return isCompatibleType(bean, mismatch)
        && hasNoExtraProperties(bean, mismatch)
        && failed.isEmpty();
  }

  private boolean isCompatibleType(T item, Description mismatchDescription) {
    if (!expectedBean.getClass().isAssignableFrom(item.getClass())) {
      mismatchDescription.appendText("is incompatible type: " + item.getClass().getSimpleName());
      return false;
    }
    return true;
  }

  private boolean hasNoExtraProperties(T item, Description mismatchDescription) {
    Set<String> actualPropertyNames = propertyNamesFrom(propertyDescriptorsFor(item, Object.class));
    actualPropertyNames.removeAll(propertyNames);
    if (!actualPropertyNames.isEmpty()) {
      mismatchDescription.appendText("has extra properties called " + actualPropertyNames);
      return false;
    }
    return true;
  }

  private void hasMatchingValues(T item, Description mismatchDescription) {
    propertyMatchers.stream().filter(propertyMatcher -> !propertyMatcher.matches(item)).forEach(propertyMatcher -> {
      propertyMatcher.describeMismatch(item, mismatchDescription);
      failed.add(propertyMatcher);
    });
  }

  private static <T> List<PropertyValuesMatcher.PropertyMatcher> propertyMatchersFor(T bean,
      PropertyDescriptor[] descriptors) {
    List<PropertyValuesMatcher.PropertyMatcher> result = new ArrayList<>(descriptors.length);
    for (PropertyDescriptor propertyDescriptor : descriptors) {
      result.add(new PropertyValuesMatcher.PropertyMatcher(propertyDescriptor, bean));
    }
    return result;
  }

  private static Set<String> propertyNamesFrom(PropertyDescriptor[] descriptors) {
    HashSet<String> result = new HashSet<>();
    for (PropertyDescriptor propertyDescriptor : descriptors) {
      result.add(propertyDescriptor.getDisplayName());
    }
    return result;
  }

  private static class PropertyMatcher extends DiagnosingMatcher<Object> {
    private final Method readMethod;
    private final Matcher<Object> matcher;
    private final String propertyName;

    PropertyMatcher(PropertyDescriptor descriptor, Object expectedObject) {
      this.propertyName = descriptor.getDisplayName();
      this.readMethod = descriptor.getReadMethod();
      this.matcher = equalTo(readProperty(readMethod, expectedObject));
    }

    @Override
    public boolean matches(Object actual, Description mismatch) {
      final Object actualValue = readProperty(readMethod, actual);
      if (!matcher.matches(actualValue)) {
        mismatch.appendText("\n" + propertyName + " ");
        matcher.describeMismatch(actualValue, mismatch);
        return false;
      }
      return true;
    }

    @Override
    public void describeTo(Description description) {
      description.appendText(propertyName + ": ").appendDescriptionOf(matcher);
    }
  }

  private static Object readProperty(Method method, Object target) {
    try {
      return method.invoke(target, NO_ARGUMENTS);
    } catch (Exception e) {
      throw new IllegalArgumentException("Could not invoke " + method + " on " + target, e);
    }
  }

  /**
   * Creates a matcher that matches when the examined object has values for all of its JavaBean properties that are
   * equal to the corresponding values of the specified bean.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(myBean, samePropertyValuesAs(myExpectedBean))
   * </pre>
   *
   * @param expectedBean the bean against which examined beans are compared
   */
  @Factory
  public static <T> Matcher<T> samePropertyValuesAs(T expectedBean) {
    return new PropertyValuesMatcher<>(expectedBean);
  }

}
