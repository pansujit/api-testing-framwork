package com.glide.qa;

import com.glide.qa.config.logging.TestLogListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Listeners;

/**
 * @author sujitpandey
 */

@Listeners({TestLogListener.class})
@ContextConfiguration(locations = {"classpath:config/rcim-config.xml"})
public abstract class BaseAbstract extends AbstractTestNGSpringContextTests {
  public static final org.apache.logging.log4j.Logger LOG =
      org.apache.logging.log4j.LogManager.getLogger(BaseAbstract.class);



}
