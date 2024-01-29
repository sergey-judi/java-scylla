package org.example.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Table("currency_conversion")
public class CurrencyConversion {

  @PrimaryKey
  CompositeKey key;

  @Column
  BigDecimal rate;

}
