package org.example.repository;

import org.example.entity.CompositeKey;
import org.example.entity.CurrencyConversion;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversionRepository extends ReactiveCassandraRepository<CurrencyConversion, CompositeKey> {}
