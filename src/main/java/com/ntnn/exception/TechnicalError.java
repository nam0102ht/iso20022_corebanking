package com.ntnn.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum TechnicalError {
  TECHNICAL_ERROR("247", "Technical Difficulties");

  private String errorCode;
  private String message;
}
