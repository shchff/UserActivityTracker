package com.shchff.producer_service.mapper;

import com.shchff.producer_service.dto.UserEventDto;
import com.shchff.producer_service.model.UserEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserEventMapperImpl.class)
public class UserEventMapperTest
{
    @Autowired
    private UserEventMapper userEventMapper;

    @Test
    void shouldMapDtoToEntity()
    {
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
