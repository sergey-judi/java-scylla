package org.example.service;

import org.example.entity.CurrencyConversion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface CurrencyConversionService {

  Flux<CurrencyConversion> getAll();

  Mono<CurrencyConversion> getById(String baseCurrency, String quoteCurrency);

  Mono<CurrencyConversion> saveWithTtl(String baseCurrency, String quoteCurrency, BigDecimal rate, Integer ttl);

}
