package org.example.contoller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.example.entity.CompositeKey;
import org.example.entity.CurrencyConversion;
import org.example.repository.ConversionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/conversions")
public class ConversionController {

  final ConversionRepository conversionRepository;

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

}
