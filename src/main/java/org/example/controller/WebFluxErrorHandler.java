//package org.example.controller;
//
//import lombok.extern.slf4j.Slf4j;
//import org.example.utility.LoggingUtils;
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.MediaType;
//import org.springframework.http.ProblemDetail;
//import org.springframework.http.codec.HttpMessageWriter;
//import org.springframework.http.codec.ServerCodecConfigurer;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import org.springframework.web.reactive.result.view.ViewResolver;
//import org.springframework.web.server.ResponseStatusException;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebExceptionHandler;
//import reactor.core.publisher.Mono;
//
//import java.net.URI;
//import java.util.Collections;
//import java.util.List;
//
//import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
//
//@Slf4j
//@Component
//@Order(HIGHEST_PRECEDENCE)
//public class WebFluxErrorHandler implements WebExceptionHandler {
//
//  private final List<HttpMessageWriter<?>> messageWriters;
//
//  private final List<ViewResolver> viewResolvers;
//
//  public WebFluxErrorHandler(ServerCodecConfigurer serverCodecConfigurer,
//                             ObjectProvider<List<ViewResolver>> viewResolversProvider) {
//    this.messageWriters = serverCodecConfigurer.getWriters();
//    this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
//  }
//
//  @Override
//  public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
//    ServerHttpRequest request = exchange.getRequest();
//    LoggingUtils.logRequest(request.getURI(), request.getMethod(), request.getHeaders(), "");
//
//    if (ex instanceof ResponseStatusException) {
//      log.error("Handling controller parameters exception [{}]", ex.getMessage());
//      return sendErrorResponse(exchange, HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getClass());
//    }
//
//    log.error("Handling internal exception [{}]", ex.getMessage());
//    return sendErrorResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex.getClass());
//  }
//
//  private Mono<Void> sendErrorResponse(
//      ServerWebExchange exchange,
//      HttpStatusCode httpStatusCode,
//      String message,
//      Class<? extends Throwable> exceptionClass
//  ) {
//    return sendErrorResponse(
//        exchange,
//        build(httpStatusCode, message, exceptionClass),
//        httpStatusCode
//    );
//  }
//
//  private Mono<Void> sendErrorResponse(ServerWebExchange exchange, ProblemDetail body, HttpStatusCode httpStatus) {
//    LoggingUtils.logResponse(httpStatus, exchange.getResponse().getHeaders(), body);
//
//    return ServerResponse.status(httpStatus)
//        .contentType(MediaType.APPLICATION_JSON)
//        .body(BodyInserters.fromValue(body))
//        .flatMap(response -> response.writeTo(exchange, new ErrorResponseContext()));
//  }
//
//  private class ErrorResponseContext implements ServerResponse.Context {
//
//    @Override
//    public List<HttpMessageWriter<?>> messageWriters() {
//      return WebFluxErrorHandler.this.messageWriters;
//    }
//
//    @Override
//    public List<ViewResolver> viewResolvers() {
//      return WebFluxErrorHandler.this.viewResolvers;
//    }
//  }
//
//  private ProblemDetail build(HttpStatusCode httpStatusCode, String message, Class<? extends Throwable> exceptionClass) {
//    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatusCode, message);
//    problemDetail.setType(URI.create(exceptionClass.getName()));
//
//    return problemDetail;
//  }
//
//}
//
