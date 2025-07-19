package com.shchff.producer_service.controller;

import com.shchff.producer_service.dto.UserEventDto;
import com.shchff.producer_service.model.UserEvent;
import com.shchff.producer_service.service.UserEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class UserEventController
{
    private final UserEventService userEventService;

    @PostMapping
    public ResponseEntity<String> postEvent(@RequestBody UserEventDto eventDto)
    {
        userEventService.sendEvent(eventDto);
        return new ResponseEntity<>("Данные сохранены", HttpStatus.CREATED);
    }
}