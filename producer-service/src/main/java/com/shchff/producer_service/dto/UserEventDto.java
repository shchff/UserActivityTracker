package com.shchff.producer_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEventDto
{
    @NotBlank(message = "userId must not be blank")
    private String userId;
    @NotBlank(message = "eventType must not be blank")
    private String eventType;
    private Map<String, String> metadata;
}