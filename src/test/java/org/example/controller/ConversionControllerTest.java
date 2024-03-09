package org.example.controller;

import lombok.SneakyThrows;
import org.example.AbstractBaseTest;
import org.example.controller.dto.CurrencyConversionDto;
import org.example.entity.CurrencyConversion;
import org.example.exception.EntityNotFoundException;
import org.example.utility.TestDataUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MissingRequestValueException;

class ConversionControllerTest extends AbstractBaseTest {

  @Test
  @SneakyThrows
  void getConversion_multiple_isOk() {
    CurrencyConversion currencyConversion = TestDataUtils.buildCurrencyConversion();
    CurrencyConversion otherCurrencyConversion = TestDataUtils.buildCurrencyConversion();

    saveEntities(currencyConversion, otherCurrencyConversion);

    webTestClient.get()
        .uri("/conversions")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("length()").isEqualTo(2);
  }

  @Test
  @SneakyThrows
  void getConversion_single_isOk() {
    CurrencyConversion currencyConversion = TestDataUtils.buildCurrencyConversion();

    saveEntities(currencyConversion);

    webTestClient.get()
        .uri(
            "/conversions/single?baseCurrency={baseCurrency}&quoteCurrency={quoteCurrency}",
            currencyConversion.getKey().getBaseCurrency(), currencyConversion.getKey().getQuoteCurrency()
        )
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("baseCurrency").isEqualTo(currencyConversion.getKey().getBaseCurrency())
        .jsonPath("quoteCurrency").isEqualTo(currencyConversion.getKey().getQuoteCurrency())
        .jsonPath("rate").isEqualTo(currencyConversion.getRate());
  }

  @Test
  @SneakyThrows
  void getConversion_notExisting_isError() {
    webTestClient.get()
        .uri(
            "/conversions/single?baseCurrency={baseCurrency}&quoteCurrency={quoteCurrency}",
            "any", "any"
        )
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound()
        .expectBody()
        .jsonPath("type").isEqualTo(EntityNotFoundException.class.getName())
        .jsonPath("title").isEqualTo("Not Found")
        .jsonPath("status").isEqualTo(404)
        .jsonPath("detail").isEqualTo("Conversion is not found for baseCurrency=[any] and quoteCurrency=[any]")
        .jsonPath("instance").isEqualTo("/conversions/single");
  }

  @Test
  @SneakyThrows
  void getConversion_withoutRequiredParams_isError() {
    webTestClient.get()
        .uri("/conversions/single")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody()
        .jsonPath("type").isEqualTo(MissingRequestValueException.class.getName())
        .jsonPath("title").isEqualTo("Bad Request")
        .jsonPath("status").isEqualTo(400)
        .jsonPath("detail").value(Matchers.containsString("Required query parameter 'baseCurrency' is not present."))
        .jsonPath("instance").isEqualTo("/conversions/single");
  }

  @Test
  @SneakyThrows
  void createConversion_isOk() {
    CurrencyConversionDto request = TestDataUtils.buildCurrencyConversionDto();

    webTestClient.post()
        .uri("/conversions/single?ttl={ttl}", TestDataUtils.DEFAULT_TTL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("baseCurrency").isEqualTo(request.baseCurrency())
        .jsonPath("quoteCurrency").isEqualTo(request.quoteCurrency())
        .jsonPath("rate").isEqualTo(request.rate());

    webTestClient.get()
        .uri(
            "/conversions/single?baseCurrency={baseCurrency}&quoteCurrency={quoteCurrency}",
            request.baseCurrency(), request.quoteCurrency()
        )
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("baseCurrency").isEqualTo(request.baseCurrency())
        .jsonPath("quoteCurrency").isEqualTo(request.quoteCurrency())
        .jsonPath("rate").isEqualTo(request.rate());
  }

  @Test
  @SneakyThrows
  void createConversion_constraintViolation_isError() {
    CurrencyConversionDto request = CurrencyConversionDto.builder().build();

    webTestClient.post()
        .uri("/conversions/single?ttl={ttl}", TestDataUtils.DEFAULT_TTL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody()
        .jsonPath("type").isEqualTo(WebExchangeBindException.class.getName())
        .jsonPath("title").isEqualTo("Bad Request")
        .jsonPath("status").isEqualTo(400)
        .jsonPath("detail").value(Matchers.containsString("baseCurrency:"))
        .jsonPath("detail").value(Matchers.containsString("quoteCurrency:"))
        .jsonPath("detail").value(Matchers.containsString("rate:"))
        .jsonPath("instance").isEqualTo("/conversions/single");
  }

}
