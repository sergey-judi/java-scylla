package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MissingRequestValueException;

import java.net.URI;

@Slf4j
@RestControllerAdvice(assignableTypes = ConversionController.class)
public class ErrorHandler {

  @ExceptionHandler(MissingRequestValueException.class)
  public ProblemDetail onMissingRequestValueException(MissingRequestValueException ex) {
    log.error("MissingRequestValueException with message=[{}]", ex.getMessage());

    return build(
        HttpStatus.BAD_REQUEST,
        ex.getMessage(),
        ex.getClass()
    );
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail onException(Exception ex) {
    log.error("Unexpected exception with message=[{}]", ex.getMessage());

    return build(
        HttpStatus.INTERNAL_SERVER_ERROR,
        ex.getMessage(),
        ex.getClass()
    );
  }

  private ProblemDetail build(HttpStatusCode httpStatusCode, String message, Class<? extends Exception> exceptionClass) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatusCode, message);
    problemDetail.setType(URI.create(exceptionClass.getName()));

    return problemDetail;
  }

}
