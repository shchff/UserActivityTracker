package com.shchff.query_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserEventByTypeDto
{
    private final UUID eventId;

    private final String eventType;

    private final Instant eventTimestamp;

    private final String userId;

    private final Map<String, String> metadata;
}
