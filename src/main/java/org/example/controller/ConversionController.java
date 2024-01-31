package org.example.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.entity.CompositeKey;
import org.example.entity.CurrencyConversion;
import org.example.repository.ConversionRepository;
import org.example.repository.CustomConversionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/conversions")
public class ConversionController {

  final ConversionRepository conversionRepository;
  final CustomConversionRepository customConversionRepository;

  @GetMapping
  public Flux<CurrencyConversion> getAllCurrencyConversions() {
    return conversionRepository.findAll();
  }

  @GetMapping("/single")
  public Mono<CurrencyConversion> getSingleCurrencyConversion(
      @NotBlank @RequestParam String baseCurrency,
      @NotBlank @RequestParam String quoteCurrency
  ) {
    return conversionRepository.findById(
        new CompositeKey()
            .setBaseCurrency(baseCurrency)
            .setQuoteCurrency(quoteCurrency)
    );
  }

  @PostMapping("/single")
  public Mono<CurrencyConversion> saveSingleCurrencyConversion(
      @NotBlank @RequestParam String baseCurrency,
      @NotBlank @RequestParam String quoteCurrency,
      @NotNull @RequestParam BigDecimal rate,
      @NotNull @RequestParam Integer ttl
  ) {
    CurrencyConversion currencyConversion = new CurrencyConversion()
        .setKey(
            new CompositeKey()
                .setBaseCurrency(baseCurrency)
                .setQuoteCurrency(quoteCurrency)
        )
        .setRate(rate);

    return customConversionRepository.save(currencyConversion, Duration.ofSeconds(ttl));
  }

}
