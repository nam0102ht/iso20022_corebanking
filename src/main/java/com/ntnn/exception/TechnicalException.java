package com.ntnn.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicalException extends RuntimeException {
  private String message;
  private String errorCode;

  public TechnicalException(String errorCode, String message) {
    this.message = message;
  }
}
