package com.shchff.consumer_service.listener;

import com.shchff.common.model.UserEvent;
import com.shchff.consumer_service.service.CassandraPersistenceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener
{
    private final CassandraPersistenceService cassandraPersistenceService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserEventListener.class);

    @KafkaListener(topics = "${app.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void handle(
            @Payload UserEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset)
    {
        LOGGER.info("Received event: id={}, type={}, userId={}, partition={}, offset={}",
                event.getId(),
                event.getEventType(),
                event.getUserId(),
                partition,
                offset
        );

        cassandraPersistenceService.save(event);
    }
}