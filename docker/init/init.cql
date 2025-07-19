CREATE KEYSPACE IF NOT EXISTS user_events_ks
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1}
AND durable_writes = true;

USE user_events_ks;

CREATE TABLE IF NOT EXISTS user_total_activity (
    user_id text PRIMARY KEY,
    event_count counter
);

CREATE TABLE IF NOT EXISTS user_events_by_type (
    event_type text,
    event_timestamp timestamp,
    event_id uuid,
    user_id text,
    metadata map<text, text>,
    PRIMARY KEY (event_type, event_timestamp, event_id)
) WITH CLUSTERING ORDER BY (event_timestamp DESC);
