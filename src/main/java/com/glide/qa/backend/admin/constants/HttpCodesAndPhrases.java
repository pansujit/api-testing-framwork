package com.glide.qa.backend.admin.constants;

/**
 * This class contains all the HTTP error code and messages.
 *
 * @author sujitpandey
 *
 */
public class HttpCodesAndPhrases {

  private HttpCodesAndPhrases() {

  }

  // http error codes
  public static final int FORBIDDEN_CODE = 403;
  public static final int BAD_REQUEST_CODE = 400;
  public static final int UNPROCESSABLE_ENTITY_CODE = 422;
  public static final int UNAUTHORISED_CODE = 401;
  public static final int NOT_FOUND_CODE = 404;

  // ------ Response status code ------
  public static final String BAD_REQUEST = "Bad Request";
  public static final String UNPROCESSABLE_ENTITY = "Unprocessable Entity";
  public static final String FORBIDDEN = "Forbidden";
  public static final String UNAUTHORISED = "Invalid authentication token";
  public static final String NOT_FOUND = "Not Found";



}
