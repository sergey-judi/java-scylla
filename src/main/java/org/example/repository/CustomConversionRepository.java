package org.example.repository;

import org.example.entity.CurrencyConversion;
import reactor.core.publisher.Mono;

import java.time.Duration;

public interface CustomConversionRepository {
  Mono<CurrencyConversion> save(CurrencyConversion currencyConversion, Duration ttl);
}
