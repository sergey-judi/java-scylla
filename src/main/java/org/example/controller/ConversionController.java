package org.example.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.controller.dto.CurrencyConversionDto;
import org.example.service.CurrencyConversionService;
import org.example.utility.CurrencyConversionMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/conversions")
public class ConversionController {

  private final CurrencyConversionService currencyConversionService;

  @GetMapping
  public Flux<CurrencyConversionDto> getAllCurrencyConversions() {
    return currencyConversionService.getAll()
        .map(CurrencyConversionMapper::toDto);
  }

  @GetMapping("/single")
  public Mono<CurrencyConversionDto> getSingleCurrencyConversion(
      @NotBlank @RequestParam String baseCurrency,
      @NotBlank @RequestParam String quoteCurrency
  ) {
    return currencyConversionService.getById(baseCurrency, quoteCurrency).
        map(CurrencyConversionMapper::toDto);
  }

  @PostMapping("/single")
  public Mono<CurrencyConversionDto> saveSingleCurrencyConversion(
      @NotBlank @RequestParam String baseCurrency,
      @NotBlank @RequestParam String quoteCurrency,
      @NotNull @RequestParam BigDecimal rate,
      @NotNull @RequestParam Integer ttl
  ) {
    return currencyConversionService.saveWithTtl(baseCurrency, quoteCurrency, rate, ttl)
        .map(CurrencyConversionMapper::toDto);
  }

}
