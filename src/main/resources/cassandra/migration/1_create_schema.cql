-- !!! IMPORTANT: keyspace MUST be created before migrations are executed !!!
-- CREATE KEYSPACE IF NOT EXISTS conversion WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 } AND durable_writes = true;

CREATE TABLE IF NOT EXISTS conversion.currency_conversion (
    base_currency TEXT,
    quote_currency TEXT,
    rate DECIMAL,
    PRIMARY KEY (base_currency, quote_currency)
);
