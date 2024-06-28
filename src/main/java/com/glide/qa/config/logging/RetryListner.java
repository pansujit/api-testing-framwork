package com.glide.qa.config.logging;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/**
 * This class retry the testng failed test.
 *
 * @author sujitpandey
 *
 */
public class RetryListner implements IAnnotationTransformer {

  @Override
  public void transform(ITestAnnotation annotation, @SuppressWarnings("rawtypes") Class testClass,
      @SuppressWarnings("rawtypes") Constructor testConstructor, Method testMethod) {
    annotation.setRetryAnalyzer(com.glide.qa.config.logging.RetryFailedTestAnalyzer.class);
  }

}
