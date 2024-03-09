package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.example.config.ScyllaConfiguration;
import org.example.entity.CurrencyConversion;
import org.example.repository.ConversionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.Arrays;

@SpringBootTest
@AutoConfigureWebTestClient
@Import(ScyllaConfiguration.class)
public abstract class AbstractBaseTest {

  @Autowired
  protected WebTestClient webTestClient;

  @Autowired
  protected ConversionRepository conversionRepository;

  @BeforeEach
  void setup() {
    conversionRepository.deleteAll().block();
  }

  public void saveEntities(CurrencyConversion... entities) {
    StepVerifier.create(
            conversionRepository.saveAll(Arrays.asList(entities))
        )
        .expectNextCount(entities.length)
        .expectComplete()
        .verify();
  }

}
