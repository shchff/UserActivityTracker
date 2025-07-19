package com.shchff.producer_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEventDto
{
    private String userId;
    private String eventType;
    private Map<String, String> metadata;
}