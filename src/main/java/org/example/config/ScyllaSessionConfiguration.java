package org.example.config;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import org.cognitor.cassandra.migration.spring.CassandraMigrationAutoConfiguration;
import org.example.JavaScyllaApplication;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ScyllaSessionConfiguration {

  @Bean
  @Qualifier(CassandraMigrationAutoConfiguration.CQL_SESSION_BEAN_NAME)
  public CqlSession cassandraMigrationCqlSession() {
    DriverConfigLoader driverConfigLoader = DriverConfigLoader.fromDefaults(
        JavaScyllaApplication.class.getClassLoader()
    );

    return CqlSession.builder()
        .withConfigLoader(driverConfigLoader)
        .build();
  }

  @Bean
  @Primary
  public CqlSession applicationCqlSession(CqlSessionBuilder cqlSessionBuilder) {
    return cqlSessionBuilder.build();
  }

}
