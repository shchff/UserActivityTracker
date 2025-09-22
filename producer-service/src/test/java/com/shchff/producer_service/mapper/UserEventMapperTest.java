package com.shchff.producer_service.mapper;

import com.shchff.common.model.UserEvent;
import com.shchff.producer_service.dto.UserEventDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class UserEventMapperTest {

    private final UserEventMapper userEventMapper = Mappers.getMapper(UserEventMapper.class);

    @Test
    void shouldMapDtoToEntity() {
        // Given
        UserEventDto dto = new UserEventDto("user123", "LOGIN", Map.of("ip", "127.0.0.1"));

        // When
        UserEvent event = userEventMapper.toEntity(dto);

        // Then
        assertNotNull(event);
        assertEquals("user123", event.getUserId());
        assertEquals("LOGIN", event.getEventType());
        assertEquals("127.0.0.1", event.getMetadata().get("ip"));

        assertNotNull(event.getId());
        assertNotNull(event.getTimestamp());
        assertTrue(event.getTimestamp().isBefore(Instant.now())
                || event.getTimestamp().equals(Instant.now()));
    }
}