# Sample Java & ScyllaDB Application
This is a sandbox project intended to demonstrate functionality of scylla db migrations & data manipulation using standard repository provided by `spring-data-cassandra-reactive`

## Table of contents
- [Migrations](#migrations)
- [Repository](#repository)
- [Configuration](#configuration)
- [Primary & Clustering Keys](#primary-and-clustering-keys)

### Migrations
External `org.cognitor.cassandra:cassandra-migration-spring-boot-starter` dependency is used to run migrations on target keyspace.
Migrations are stored at `resources/cassandra/migration` by default
```yaml
cassandra:
  migration:
    keyspace-name: "${spring.cassandra.keyspace-name}"
    with-consensus: true
    strategy: fail_on_duplicates
    consistency-level: all
```

### Repository
Repository extends canonical `ReactiveCassandraRepository`, with the use of functionality of auto-generated queries & custom-written ones
```java
@Repository
public interface ConversionRepository extends ReactiveCassandraRepository<CurrencyConversion, CompositeKey> {

  // example with generated query
  @AllowFiltering // better to avoid
  Mono<CurrencyConversion> getByKeyBaseCurrencyAndKeyQuoteCurrencyAndRateGreaterThanEqual(String baseCurrency, String quoteCurrency, BigDecimal rate);

  // example with custom query
  @Query("SELECT base_currency, quote_currency, rate FROM currency_conversion WHERE base_currency=?0 AND quote_currency=?1")
  Mono<CurrencyConversion> getWithCustomQuery(String baseCurrency, String quoteCurrency);

}
```

Composite key is described as standalone model carrying columns that make up the key of the table - additionally clustering column can be added if needed
```java
@Data
@PrimaryKeyClass
public class CompositeKey {

  @PrimaryKeyColumn(name = "base_currency", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
  @CassandraType(type = CassandraType.Name.TEXT)
  String baseCurrency;

  @PrimaryKeyColumn(name = "quote_currency", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
  @CassandraType(type = CassandraType.Name.TEXT)
  String quoteCurrency;

}
```

### Configuration
Dedicated migrational session MUST be created to exclude its usage in underlying service logic
```java
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
```

For more details see [library source code & description](https://github.com/patka/cassandra-migration)

### Primary and Clustering Keys
When declaring table schema it's important to define primary key correctly 

`PRIMARY KEY ((base_currency, quote_currency))` - composite primary key of two columns `base_currency` & `quote_currency`

`PRIMARY KEY (base_currency, quote_currency)` - `base_currency` is the primary key whereas `quote_currency` is clustering key
