package org.example.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.controller.dto.CurrencyConversionDto;
import org.example.entity.CompositeKey;
import org.example.entity.CurrencyConversion;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestDataUtils {

  public static final BigDecimal DEFAULT_RATE = BigDecimal.valueOf(2.68);
  public static final int DEFAULT_TTL = 5;

  private static final AtomicLong counter = new AtomicLong();

  public static CurrencyConversion buildCurrencyConversion() {
    return new CurrencyConversion()
        .setKey(
            new CompositeKey()
                .setBaseCurrency(uniqueId("base-currency"))
                .setQuoteCurrency(uniqueId("quote-currency"))
        )
        .setRate(DEFAULT_RATE);
  }

  public static CurrencyConversionDto buildCurrencyConversionDto() {
    return CurrencyConversionDto.builder()
        .baseCurrency(uniqueId("base-currency"))
        .quoteCurrency(uniqueId("quote-currency"))
        .rate(DEFAULT_RATE)
        .build();
  }

  public static String uniqueId(String value) {
    TestDataUtils.counter.incrementAndGet();

    return "%s-%s".formatted(value, counter.get());
  }

}
