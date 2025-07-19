package com.shchff.query_service.service;

import com.shchff.query_service.dto.UserEventByTypeDto;
import com.shchff.query_service.model.UserTotalActivity;

import java.util.List;

public interface UserEventQueryService
{
    List<UserTotalActivity> getTopUsers(int limit);
    List<UserEventByTypeDto> getEventsByType(String type);
}
