package com.glide.qa.config.logging;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.extern.log4j.Log4j2;
import org.testng.Reporter;

/**
 * This is custom filter class from where we logged all the request and response.
 *
 * @author sujitpandey
 *
 */
@Log4j2
public class CustomLogFilter implements Filter {

  @Override
  public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec,
      FilterContext ctx) {


    log.info("Base URI: " + requestSpec.getURI());
    log.info("HTTP Method:  " + requestSpec.getMethod());
    Reporter.log("Base URI: " + requestSpec.getURI());
    Reporter.log("HTTP Method:  " + requestSpec.getMethod());

    if (!requestSpec.getFormParams().isEmpty()) {
      log.info("form data: " + requestSpec.getFormParams().toString());
    }

    if (requestSpec.getHeaders().get("X-AUTH-TOKEN") != null) {
      log.info("X-AUTH-TOKEN: " + requestSpec.getHeaders().get("X-AUTH-TOKEN").getValue());
    }

    if (requestSpec.getBody() != null) {
      log.info("Request Body: " + requestSpec.getBody());
    }

    Response response = ctx.next(requestSpec, responseSpec);

    if (!response.getBody().asString().isEmpty()) {
      log.info("Response Body: " + response.getBody().asString());
      Reporter.log("Response Body: " + response.getBody().asString());

    }

    log.info("Status Code: " + response.getStatusCode());
    Reporter.log("Status Code: " + response.getStatusCode());

    return response;
  }
}
