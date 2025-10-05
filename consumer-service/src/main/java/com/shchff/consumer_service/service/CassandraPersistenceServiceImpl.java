package com.shchff.consumer_service.service;

import com.shchff.common.model.UserEvent;
import com.shchff.consumer_service.model.UserEventByType;
import com.shchff.consumer_service.repository.UserEventByTypeRepository;
import com.shchff.consumer_service.repository.UserTotalActivityRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CassandraPersistenceServiceImpl implements CassandraPersistenceService
{
    private final UserEventByTypeRepository eventByTypeRepository;
    private final UserTotalActivityRepository userTotalActivityRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    @Override
    public void save(UserEvent event)
    {
        saveByType(event);
        userTotalActivityRepository.incrementCounter(event.getUserId());

        LOGGER.info("Saved event: userId={}, eventType={}", event.getUserId(), event.getEventType());
    }

    private void saveByType(UserEvent event)
    {
        UserEventByType.Key key = new UserEventByType.Key(
                event.getEventType(),
                event.getTimestamp(),
                event.getId()
        );


        UserEventByType eventByType = new UserEventByType(
                key,
                event.getUserId(),
                event.getMetadata()
        );

        eventByTypeRepository.save(eventByType);
    }
}
