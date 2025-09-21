package com.shchff.producer_service.service;

import com.shchff.producer_service.dto.UserEventDto;
import com.shchff.producer_service.mapper.UserEventMapper;
import com.shchff.producer_service.model.UserEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserEventServiceImpl implements UserEventService
{
    @Value("${app.topic.name}")
    private String topic;

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserEventServiceImpl.class);

    private final UserEventMapper userEventMapper;

    @Override
    public void sendEvent(UserEventDto eventDto)
    {
        UserEvent event = userEventMapper.toEntity(eventDto);

        String key = Objects.requireNonNullElse(event.getUserId(), "unknown-user");

        kafkaTemplate.send(topic, key, event)
                .whenComplete((result, exception) -> {
                    if (exception != null)
                    {
                        LOGGER.error("Failed to send message: id={}, userId={}",
                                event.getId(), event.getUserId(), exception);
                    }
                    else
                    {
                        LOGGER.info("Message sent: id={}, userId={}, topic={}, partition={}, offset={}",
                                event.getId(),
                                event.getUserId(),
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                })
                .exceptionallyCompose(e -> {
                    LOGGER.warn("Retrying send for eventId={} due to error={}",
                            event.getId(), e.getMessage());

                    return kafkaTemplate.send(topic, key, event);
                });
    }
}