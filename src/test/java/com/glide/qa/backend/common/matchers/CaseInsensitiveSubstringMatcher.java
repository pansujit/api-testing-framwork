package com.glide.qa.backend.common.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.testng.annotations.Factory;

/**
 * This matcher will check for contains given substring in given string in case insensitive manner.
 *
 * @author sujitpandey
 *
 */
public class CaseInsensitiveSubstringMatcher extends TypeSafeMatcher<String> {

  private final String subString;

  private CaseInsensitiveSubstringMatcher(final String subString) {
    this.subString = subString;
  }


  @Override
  protected boolean matchesSafely(final String actualString) {
    return actualString.toLowerCase().contains(this.subString.toLowerCase());
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("containing substring \"" + this.subString + "\"");
  }

  @Factory
  public static Matcher<String> containsIgnoringCase(final String subString) {
    return new CaseInsensitiveSubstringMatcher(subString);
  }



}
