package com.shchff.consumer_service.handler;


import com.shchff.consumer_service.model.*;
import com.shchff.consumer_service.service.CassandraPersistenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserEventHandler
{
    private final CassandraPersistenceService cassandraPersistenceService;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @KafkaListener(topics = "${app.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void handle(@Payload UserEvent event)
    {
        LOGGER.info("Received event: {}", event.getEventType());
        cassandraPersistenceService.save(event);
    }
}