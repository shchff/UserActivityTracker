package com.shchff.consumer_service.service;

import com.shchff.consumer_service.model.UserEvent;
import com.shchff.consumer_service.model.UserEventByType;
import com.shchff.consumer_service.repository.UserEventByTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CassandraPersistenceServiceImpl implements CassandraPersistenceService
{
    private final UserEventByTypeRepository eventByTypeRepository;
    private final CqlTemplate cqlTemplate;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final String updateScript =
            "UPDATE user_total_activity SET event_count = event_count + 1 WHERE user_id = ?";

    @Transactional
    @Override
    public void save(UserEvent event)
    {
        saveByType(event);
        saveActivity(event);

        LOGGER.info("Event saved");
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


    private void saveActivity(UserEvent event)
    {
        cqlTemplate.execute(updateScript, event.getUserId());
    }
}
