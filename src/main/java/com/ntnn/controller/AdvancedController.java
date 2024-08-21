package com.ntnn.controller;

import com.ntnn.common.ResponseErrorMessage;
import com.ntnn.exception.BadRequestException;
import com.ntnn.exception.TechnicalException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.xml.bind.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class AdvancedController {

  @ExceptionHandler({ValidationException.class, ConstraintViolationException.class, MissingServletRequestParameterException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ResponseErrorMessage> processErrorInvalid(Exception ex) {
    ResponseErrorMessage res = new ResponseErrorMessage();
    res.setCode(400);
    res.setStatus("Error");
    res.setMessage(ex.getMessage());
    log.error("Validation exception with message is='{}'", ex.getMessage());
    return new ResponseEntity<>(res, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({BadRequestException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ResponseErrorMessage> badRequest(Exception ex) {
    ResponseErrorMessage res = new ResponseErrorMessage();
    res.setCode(400);
    res.setStatus("Error");
    res.setMessage(ex.getMessage());
    log.error("Validation exception with message is='{}'", ex.getMessage());
    return new ResponseEntity<>(res, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({TechnicalException.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ResponseErrorMessage> processTechnicalException(Exception ex) {
    ResponseErrorMessage res = new ResponseErrorMessage();
    res.setCode(500);
    res.setStatus("Error");
    res.setMessage(ex.getSuppressed()[0].getMessage());
    log.error("Technical exception with message is='{}'", Arrays.toString(ex.getSuppressed()));
    return new ResponseEntity<>(res, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({RequestNotPermitted.class})
  @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
  public ResponseEntity<ResponseErrorMessage> tooManyConnection(Exception ex) {
    ResponseErrorMessage res = new ResponseErrorMessage();
    res.setCode(429);
    res.setStatus("Error");
    res.setMessage(ex.getMessage());
    log.error("Too many request ='{}'", ex.getMessage());
    return new ResponseEntity<>(res, new HttpHeaders(), HttpStatus.TOO_MANY_REQUESTS);
  }
}
