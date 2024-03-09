package org.example.controller.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CurrencyConversionDto(
    String baseCurrency,
    String quoteCurrency,
    BigDecimal rate
) {}
