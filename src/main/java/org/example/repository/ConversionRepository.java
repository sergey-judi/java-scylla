package org.example.repository;

import org.example.entity.CompositeKey;
import org.example.entity.CurrencyConversion;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Repository
public interface ConversionRepository
    extends CustomConversionRepository, ReactiveCassandraRepository<CurrencyConversion, CompositeKey>  {

  // example with generated query
  @AllowFiltering // better to avoid
  Mono<CurrencyConversion> getByKeyBaseCurrencyAndKeyQuoteCurrencyAndRateGreaterThanEqual(String baseCurrency, String quoteCurrency, BigDecimal rate);

  // example with custom query
  @Query("SELECT base_currency, quote_currency, rate FROM currency_conversion WHERE base_currency=?0 AND quote_currency=?1")
  Mono<CurrencyConversion> getWithCustomQuery(String baseCurrency, String quoteCurrency);

}
