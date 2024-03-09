package org.example.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.utility.DockerImageName;

public class ScyllaContainer extends CassandraContainer<ScyllaContainer> {

  private final CassandraProperties cassandraProperties;

  public ScyllaContainer(CassandraProperties cassandraProperties) {
    super(
        DockerImageName.parse("scylladb/scylla:5.4.1")
            .asCompatibleSubstituteFor("cassandra")
    );

    this.cassandraProperties = cassandraProperties;
  }

  public ScyllaContainer await() {
    this.start();
    return this;
  }

  public ScyllaContainer initKeyspace() {
    try (CqlSession keyspaceSession = initKeyspaceSession()) {
      keyspaceSession.execute(
          "CREATE KEYSPACE IF NOT EXISTS %s WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1}"
              .formatted(cassandraProperties.getKeyspaceName())
      );
    }

    return this;
  }

  private CqlSession initKeyspaceSession() {
    return CqlSession.builder()
        .addContactPoint(this.getContactPoint())
        .withLocalDatacenter(this.getLocalDatacenter())
        .build();
  }

}
