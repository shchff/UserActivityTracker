package com.shchff.producer_service.service;

import com.shchff.producer_service.dto.UserEventDto;

public interface UserEventService
{
    void sendEvent(UserEventDto event);
}
