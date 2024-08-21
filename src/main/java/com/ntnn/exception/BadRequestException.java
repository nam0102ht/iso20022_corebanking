package com.ntnn.exception;

public class BadRequestException extends RuntimeException {
  private String message;

  public BadRequestException(String message) {
    super(message);
    this.addSuppressed(new Throwable(message));
  }

}
