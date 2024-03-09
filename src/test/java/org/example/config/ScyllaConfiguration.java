package org.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ScyllaConfiguration {

  private final CassandraProperties cassandraProperties;

  @Bean(destroyMethod = "stop")
  public ScyllaContainer scyllaContainer() {
    return new ScyllaContainer(cassandraProperties)
        .await()
        .initKeyspace();
  }

  @Bean
  public CqlSessionBuilderCustomizer cqlSessionBuilderCustomizer(ScyllaContainer scyllaContainer) {
    return cqlSessionBuilder -> cqlSessionBuilder
        .addContactPoint(scyllaContainer.getContactPoint())
        .withLocalDatacenter(scyllaContainer.getLocalDatacenter());
  }

}
