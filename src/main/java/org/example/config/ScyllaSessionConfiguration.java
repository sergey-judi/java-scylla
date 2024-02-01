package org.example.config;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import org.cognitor.cassandra.migration.spring.CassandraMigrationAutoConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ScyllaSessionConfiguration {

  @Bean
  @Qualifier(CassandraMigrationAutoConfiguration.CQL_SESSION_BEAN_NAME)
  public CqlSession cassandraMigrationCqlSession(CqlSessionBuilder cqlSessionBuilder) {
    return cqlSessionBuilder.build();
  }

  @Bean
  @Primary
  public CqlSession applicationCqlSession(CqlSessionBuilder cqlSessionBuilder) {
    return cqlSessionBuilder.build();
  }

}
