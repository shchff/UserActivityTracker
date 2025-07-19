package com.shchff.query_service.controller;

import com.shchff.query_service.dto.UserEventByTypeDto;
import com.shchff.query_service.model.UserEventByType;
import com.shchff.query_service.model.UserTotalActivity;
import com.shchff.query_service.repository.UserEventByTypeRepository;
import com.shchff.query_service.repository.UserTotalActivityRepository;
import com.shchff.query_service.service.UserEventQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "User Events API", description = "API для работы с пользовательскими событиями")
public class EventQueryController
{
    private final UserEventQueryService queryService;

    private final UserEventByTypeRepository eventRepo;
    private final UserTotalActivityRepository activityRepo;

    @GetMapping("/top-users")
    @Operation(summary = "Получить топ пользователей по активности")
    public List<UserTotalActivity> getTopUsers(
            @RequestParam(defaultValue = "10") int limit
    )
    {
        return queryService.getTopUsers(limit);
    }

    @GetMapping
    @Operation(summary = "Получить события (с фильтрацией по типу)")
    public List<UserEventByTypeDto> getEventsByType(
            @RequestParam @NotBlank String type
    )
    {
        return queryService.getEventsByType(type);
    }
}