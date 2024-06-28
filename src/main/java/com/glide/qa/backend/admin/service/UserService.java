package com.glide.qa.backend.admin.service;

import static com.glide.qa.backend.common.util.ResponseWrapper.retrieveResponse;
import static com.glide.qa.backend.common.util.Util.LOGFILTER;
import static io.restassured.RestAssured.given;

import com.glide.qa.backend.admin.dto.ResponseDto;
import com.glide.qa.backend.admin.dto.user.UserData;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This class contains the service level implementation of user service.
 *
 * @author sujitpandey
 *
 */
@Component("user-service")
public class UserService {

  private RequestSpecification requestSpecification;

  @Value("${qa.baseUrl}")
  private String baseUrl;

  @Value("${get.users}")
  private String getUsersEndpoint;

  /**
   * This will call only once after the bean intialize.
   */
  @PostConstruct
  public void prepareData() {
    requestSpecification = new RequestSpecBuilder()
        .setBaseUri(baseUrl)
        .setContentType("application/json")
        .addFilter(LOGFILTER)
        .build();
  }




  /**
   * Retrieves users from given endpoints.
   *
   * @param
   * @return ResponseDto&lt;UserData&gt;
   */
  public ResponseDto<UserData> getUsers() {
    Response response = given(requestSpecification)
        .get(getUsersEndpoint);
    return retrieveResponse(response, UserData.class);
  }


}
