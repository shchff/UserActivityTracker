package com.shchff.producer_service.controller;

import com.shchff.producer_service.dto.UserEventDto;
import com.shchff.producer_service.service.UserEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class UserEventController
{
    private final UserEventService userEventService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserEventController.class);

    @PostMapping
    public ResponseEntity<Map<String,String>> postEvent(@Valid @RequestBody UserEventDto eventDto)
    {
        LOGGER.debug("Received event: {}", eventDto);
        userEventService.sendEvent(eventDto);
        return ResponseEntity.accepted().body(Map.of("status", "ACCEPTED"));
    }
}