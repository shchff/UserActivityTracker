package com.shchff.query_service.service;

import com.shchff.query_service.dto.UserEventByTypeDto;
import com.shchff.query_service.model.UserEventByType;
import com.shchff.query_service.model.UserTotalActivity;
import com.shchff.query_service.repository.UserEventByTypeRepository;
import com.shchff.query_service.repository.UserTotalActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserEventQueryServiceImpl implements UserEventQueryService
{
    private final UserEventByTypeRepository eventByTypeRepository;
    private final UserTotalActivityRepository activityRepository;

    @Override
    public List<UserTotalActivity> getTopUsers(int limit)
    {
        List<UserTotalActivity> allActivities = activityRepository.findAll();
        return allActivities.stream()
                .sorted(Comparator.comparingLong(UserTotalActivity::getEventCount).reversed())
                .limit(limit)
                .toList();
    }

    @Override
    public List<UserEventByTypeDto> getEventsByType(String type)
    {
        List<UserEventByType> events = eventByTypeRepository.findByKeyEventType(type);
        return events.stream()
                .map(this::toDto)
                .toList();
    }

    private UserEventByTypeDto toDto(UserEventByType event)
    {
        return new UserEventByTypeDto(
                event.getKey().getEventId(),
                event.getKey().getEventType(),
                event.getKey().getEventTimestamp(),
                event.getUserId(),
                event.getMetadata()
        );
    }
}