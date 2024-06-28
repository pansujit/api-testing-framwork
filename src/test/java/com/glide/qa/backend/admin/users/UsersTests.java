package com.glide.qa.backend.admin.users;

import com.glide.qa.backend.admin.AdminAbstract;
import com.glide.qa.backend.admin.dto.ResponseDto;
import com.glide.qa.backend.admin.dto.user.UserData;
import io.restassured.internal.http.Status;
import org.testng.annotations.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class UsersTests extends AdminAbstract {

  @Test
  public void getUsersTest(){
    ResponseDto<UserData> userResponse= userService.getUsers();
    UserData userData= userResponse.getData();
    assertThat("The status code is not correct", Status.SUCCESS.matches(userResponse.getStatus()), is(true));
    assertThat("The status code is not correct", userData.getPage(), greaterThan(0));


  }

}
