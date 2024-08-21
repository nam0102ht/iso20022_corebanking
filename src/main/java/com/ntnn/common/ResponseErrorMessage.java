package com.ntnn.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Setter
@Getter
public class ResponseErrorMessage {
  @JsonProperty("code")
  private int code;

  @JsonProperty("status")
  private String status;

  @JsonProperty("message")
  private String message;
}