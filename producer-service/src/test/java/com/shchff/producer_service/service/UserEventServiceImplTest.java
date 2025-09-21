package com.shchff.producer_service.service;

import com.shchff.producer_service.dto.UserEventDto;
import com.shchff.producer_service.mapper.UserEventMapper;
import com.shchff.producer_service.model.UserEvent;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserEventServiceImplTest
{
    @Mock
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Mock
    private UserEventMapper userEventMapper;

    @InjectMocks
    private UserEventServiceImpl userEventService;

    private final String topic = "test-topic";

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        org.springframework.test.util.ReflectionTestUtils.setField(userEventService, "topic", topic);
    }

    @Test
    void shouldSendEventSuccessfully()
    {
        // Given
        UserEventDto dto = new UserEventDto("user-123", "LOGIN", Map.of("ip", "127.0.0.1"));
        UserEvent event = new UserEvent(UUID.randomUUID(), "user-123", "LOGIN", null, Map.of("ip", "127.0.0.1"));

        when(userEventMapper.toEntity(dto)).thenReturn(event);

        CompletableFuture<SendResult<String, UserEvent>> future = new CompletableFuture<>();
        when(kafkaTemplate.send(eq(topic), eq("user-123"), eq(event))).thenReturn(future);

        // When
        userEventService.sendEvent(dto);

        // Then
        verify(kafkaTemplate).send(eq(topic), eq("user-123"), eq(event));

        RecordMetadata metadata = new RecordMetadata(
                new org.apache.kafka.common.TopicPartition(topic, 0),
                0, 0, System.currentTimeMillis(), 0L, 0, 0
        );
        SendResult<String, UserEvent> sendResult = new SendResult<>(null, metadata);
        future.complete(sendResult);
    }

    @Test
    void shouldSendWithUnknownUserKeyWhenUserIdIsNull()
    {
        // Given
        UserEventDto dto = new UserEventDto(null, "LOGIN", Map.of());
        UserEvent event = new UserEvent(UUID.randomUUID(), null, "LOGIN", null, Map.of());

        when(userEventMapper.toEntity(dto)).thenReturn(event);

        CompletableFuture<SendResult<String, UserEvent>> future = new CompletableFuture<>();
        when(kafkaTemplate.send(eq(topic), eq("unknown-user"), eq(event))).thenReturn(future);

        // When
        userEventService.sendEvent(dto);

        // Then
        verify(kafkaTemplate).send(eq(topic), eq("unknown-user"), eq(event));
    }

    @Test
    void shouldRetryWhenSendFails()
    {
        // Given
        UserEventDto dto = new UserEventDto("user-123", "LOGIN", Map.of());
        UserEvent event = new UserEvent(UUID.randomUUID(), "user-123", "LOGIN", null, Map.of());

        when(userEventMapper.toEntity(dto)).thenReturn(event);

        CompletableFuture<SendResult<String, UserEvent>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Kafka error"));

        CompletableFuture<SendResult<String, UserEvent>> retryFuture = new CompletableFuture<>();

        when(kafkaTemplate.send(eq(topic), eq("user-123"), eq(event)))
                .thenReturn(failedFuture)
                .thenReturn(retryFuture);

        // When
        userEventService.sendEvent(dto);

        // Then
        verify(kafkaTemplate, times(2)).send(eq(topic), eq("user-123"), eq(event));
    }
}
