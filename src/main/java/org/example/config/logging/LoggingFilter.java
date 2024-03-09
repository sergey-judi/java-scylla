package org.example.config.logging;

import lombok.RequiredArgsConstructor;
import org.example.utility.LoggingUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoggingFilter implements WebFilter {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    if (exchange.getRequest().getMethod().equals(HttpMethod.GET)) {
      LoggingUtils.logRequest(
          exchange.getRequest().getURI(),
          HttpMethod.GET,
          exchange.getRequest().getHeaders(),
          null
      );
    }

    return chain.filter(buildDecoratedExchange(exchange));
  }

  private ServerWebExchange buildDecoratedExchange(ServerWebExchange exchange) {
    return new ServerWebExchangeDecorator(exchange) {
      @Override
      public ServerHttpRequest getRequest() {
        return new RequestLoggingInterceptor(super.getRequest());
      }

      @Override
      public ServerHttpResponse getResponse() {
        return new ResponseLoggingInterceptor(super.getResponse());
      }
    };
  }

}