package com.shchff.consumer_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("user_events")
public class UserEvent {

    @PrimaryKey
    private UUID id;
    private String userId;
    private String eventType;
    private Instant timestamp;
    private Map<String, String> metadata;
}