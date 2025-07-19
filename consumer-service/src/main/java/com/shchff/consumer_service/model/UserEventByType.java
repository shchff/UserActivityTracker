package com.shchff.consumer_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Table("user_events_by_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEventByType
{
    @PrimaryKeyClass
    @AllArgsConstructor
    public static class Key implements Serializable
    {
        @PrimaryKeyColumn(name = "event_type", type = PrimaryKeyType.PARTITIONED)
        private String eventType;

        @PrimaryKeyColumn(name = "event_timestamp", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        private Instant eventTimestamp;

        @PrimaryKeyColumn(name = "event_id", type = PrimaryKeyType.CLUSTERED)
        private UUID eventId;
    }

    @PrimaryKey
    private Key key;

    @Column("user_id")
    private String userId;

    private Map<String, String> metadata;
}