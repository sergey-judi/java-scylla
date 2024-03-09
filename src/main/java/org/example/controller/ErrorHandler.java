package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@Slf4j
@RestControllerAdvice(assignableTypes = ConversionController.class)
public class ErrorHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public ProblemDetail onResponseStatusException(ResponseStatusException ex) {
    log.error("ResponseStatusException with message=[{}]", ex.getMessage());

    return build(
        HttpStatus.BAD_REQUEST,
        ex.getMessage(),
        ex.getClass()
    );
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ProblemDetail onEntityNotFoundException(EntityNotFoundException ex) {
    log.error("Entity not found exception with message=[{}]", ex.getMessage());

    return build(
        HttpStatus.NOT_FOUND,
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

  private ProblemDetail build(
      HttpStatusCode httpStatusCode,
      String message,
      Class<? extends Exception> exceptionClass
  ) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatusCode, message);
    problemDetail.setType(URI.create(exceptionClass.getName()));

    return problemDetail;
  }

}
