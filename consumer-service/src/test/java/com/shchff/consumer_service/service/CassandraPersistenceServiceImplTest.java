package com.shchff.consumer_service.service;

import com.shchff.common.model.UserEvent;
import com.shchff.consumer_service.model.UserEventByType;
import com.shchff.consumer_service.repository.UserEventByTypeRepository;
import com.shchff.consumer_service.repository.UserTotalActivityRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CassandraPersistenceServiceImplTest
{
    @Mock
    private UserEventByTypeRepository eventByTypeRepository;

    @Mock
    private UserTotalActivityRepository userTotalActivityRepository;

    @InjectMocks
    private CassandraPersistenceServiceImpl cassandraPersistenceService;

    @Test
    void shouldSaveEventAndIncrementActivity()
    {
        // Given
        UserEvent event = new UserEvent(
                UUID.randomUUID(),
                "user123",
                "LOGIN",
                Instant.now(),
                Map.of("ip", "127.0.0.1")
        );

        // When
        cassandraPersistenceService.save(event);

        // then
        ArgumentCaptor<UserEventByType> eventCaptor = ArgumentCaptor.forClass(UserEventByType.class);
        verify(eventByTypeRepository, times(1)).save(eventCaptor.capture());
        verify(userTotalActivityRepository, times(1)).incrementCounter("user123");

        UserEventByType savedEvent = eventCaptor.getValue();
        assertEquals("user123", savedEvent.getUserId());
        assertEquals("LOGIN", savedEvent.getKey().getEventType());
        assertEquals("127.0.0.1", savedEvent.getMetadata().get("ip"));
        assertNotNull(savedEvent.getKey().getEventId());
        assertNotNull(savedEvent.getKey().getEventTimestamp());
    }
}
