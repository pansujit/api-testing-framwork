package com.glide.qa.backend.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glide.qa.backend.admin.dto.Error;
import com.glide.qa.backend.admin.dto.ResponseDto;
import com.google.gson.Gson;
import io.restassured.response.Response;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;

/**
 * This is response wrapper class which maps all the response in tests.
 *
 * @author sujitpandey
 *
 */
@Log4j2
public class ResponseWrapper {

  private ResponseWrapper() {

  }

  private static ObjectMapper mapper = new ObjectMapper();
  private static Gson gson = new Gson();

  /**
   * This method retrieve response from given data.
   *
   * @param <T> - Should be T
   * @param response - Should be Response
   * @param type - Should be Class
   * @return ResponseDto
   */
  public static <T> ResponseDto<T> retrieveResponse(Response response, Class<T> type) {
    ResponseDto<T> responseDto = new ResponseDto<>();
    if (response.statusCode() / 100 == 2) {
      try {
        responseDto.setData(mapper.readValue(response.asString(), type));
      } catch (IOException e) {
        try {
          responseDto.setData(mapper.readValue(gson.toJson(response.asString()), type));
        } catch (IOException e1) {
          log.error(e);
        }
      }
    } else {
      try {
        responseDto.setError(mapper.readValue(response.asString(), Error.class));
      } catch (IOException e) {
        log.error(e);
      }
    }
    responseDto.setStatus(response.getStatusCode());
    return responseDto;
  }
}
