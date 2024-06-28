package com.glide.qa.backend.admin.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

/**
 * This will map the response.
 *@author sujitpandey
 * @param <T> class
 */

@Data
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseDto<T> {

  Error error;
  T data;
  int status;
}
