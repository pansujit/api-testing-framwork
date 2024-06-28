package com.glide.qa.backend.admin.data;

import javax.annotation.PostConstruct;
import lombok.Data;
import org.springframework.stereotype.Component;
;

/**
 * This will run before the test started and if fail, all test will not run.
 *
 * @author sujitpandey
 **/
@Data
@Component("test-data")
public class TestData {


  /**
   * This will initialize all data before running the tests. added couple of new things: linkb2bMemberId and
   * otherMemberId are added to create bookings. And their respective logins are b2bMemberLogin and otherMemberLogin
   * which will helpful for refactor the booking.
   */
  @PostConstruct
  public void initTestData() {

  }

}
