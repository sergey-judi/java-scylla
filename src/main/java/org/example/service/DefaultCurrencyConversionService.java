package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.CompositeKey;
import org.example.entity.CurrencyConversion;
import org.example.exception.EntityNotFoundException;
import org.example.repository.ConversionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class DefaultCurrencyConversionService implements CurrencyConversionService {

  private final ConversionRepository conversionRepository;

  @Override
  public Flux<CurrencyConversion> getAll() {
    return conversionRepository.findAll();
  }

  @Override
  public Mono<CurrencyConversion> getById(String baseCurrency, String quoteCurrency) {
    CompositeKey id = new CompositeKey()
        .setBaseCurrency(baseCurrency)
        .setQuoteCurrency(quoteCurrency);

    return conversionRepository.findById(id)
        .switchIfEmpty(
            Mono.error(
                new EntityNotFoundException(
                    "Conversion is not found for baseCurrency=[%s] and quoteCurrency=[%s]"
                        .formatted(baseCurrency, quoteCurrency)
                )
            )
        );
  }

  @Override
  public Mono<CurrencyConversion> saveWithTtl(CurrencyConversion currencyConversion, Integer ttl) {
    return conversionRepository.save(currencyConversion, Duration.ofSeconds(ttl));
  }

}
