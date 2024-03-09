package org.example.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.controller.dto.CurrencyConversionDto;
import org.example.entity.CompositeKey;
import org.example.entity.CurrencyConversion;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyConversionMapper {

  public static List<CurrencyConversionDto> toDto(List<CurrencyConversion> entities) {
    return entities.stream()
        .map(CurrencyConversionMapper::toDto)
        .toList();
  }

  public static CurrencyConversionDto toDto(CurrencyConversion entity) {
    return CurrencyConversionDto.builder()
        .baseCurrency(entity.getKey().getBaseCurrency())
        .quoteCurrency(entity.getKey().getQuoteCurrency())
        .rate(entity.getRate())
        .build();
  }

  public static CurrencyConversion toModel(CurrencyConversionDto dto) {
    return new CurrencyConversion()
        .setKey(
            new CompositeKey()
                .setBaseCurrency(dto.baseCurrency())
                .setQuoteCurrency(dto.quoteCurrency())
        )
        .setRate(dto.rate());
  }

}
