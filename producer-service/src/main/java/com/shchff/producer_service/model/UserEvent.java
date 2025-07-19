package com.shchff.producer_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEvent
{
    private UUID id;
    private String userId;
    private String eventType;
    private Instant timestamp;
    private Map<String, String> metadata;
}
