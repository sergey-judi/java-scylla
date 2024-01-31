package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.entity.CurrencyConversion;
import org.springframework.data.cassandra.core.EntityWriteResult;
import org.springframework.data.cassandra.core.InsertOptions;
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class DefaultCustomConversionRepository implements CustomConversionRepository {

  final ReactiveCassandraTemplate reactiveCassandraTemplate;

  @Override
  public Mono<CurrencyConversion> save(CurrencyConversion currencyConversion, Duration ttl) {
    InsertOptions insertOptions = InsertOptions.builder()
        .ttl(ttl)
        .build();

    return reactiveCassandraTemplate.insert(currencyConversion, insertOptions)
        .map(EntityWriteResult::getEntity);
  }

}
