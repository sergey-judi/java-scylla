package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class JavaScyllaApplication {

  public static void main(String[] args) {
    SpringApplication.run(JavaScyllaApplication.class, args);
  }

}
