package org.example.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CurrencyConversionDto(
    @NotBlank String baseCurrency,
    @NotBlank String quoteCurrency,
    @NotNull BigDecimal rate
) {}
