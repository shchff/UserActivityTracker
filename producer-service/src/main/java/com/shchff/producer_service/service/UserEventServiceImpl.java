package com.shchff.producer_service.service;

import com.shchff.producer_service.dto.UserEventDto;
import com.shchff.producer_service.model.UserEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class UserEventServiceImpl implements UserEventService
{
    @Value("${app.topic.name}")
    private String topic;

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void sendEvent(UserEventDto eventDto)
    {
        UserEvent event = new UserEvent(
                UUID.randomUUID(),
                eventDto.getUserId(),
                eventDto.getEventType(),
                Instant.now(),
                eventDto.getMetadata()
        );

        CompletableFuture<SendResult<String, UserEvent>> future =
                kafkaTemplate.send(topic, event.getUserId(), event);

        future.whenComplete((result, exception) ->
        {
            if (exception != null)
            {
                LOGGER.error("Failed to send message", exception);
            }
            else
            {
                LOGGER.info("Message is sent successfully to topic={}, partition={}, offset={}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            }
        });
    }
}