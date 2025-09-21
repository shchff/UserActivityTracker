package com.shchff.producer_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shchff.producer_service.dto.UserEventDto;
import com.shchff.producer_service.service.UserEventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserEventController.class)
@TestPropertySource(properties = {
        "app.topic.name=test-topic",
        "logging.level.com.shchff.producerservice=DEBUG"
})
public class UserEventControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserEventService userEventService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateEventSuccessfully() throws Exception
    {
        // Given
        UserEventDto eventDto = new UserEventDto(
                "user-123",
                "USER_LOGIN",
                Map.of("sessionId", "abc123", "ip", "192.168.1.1")
        );

        mockMvc.perform(post("/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Data saved"))
                .andExpect(header().string("Content-Type", "text/plain;charset=UTF-8"));

        verify(userEventService).sendEvent(eventDto);
        verifyNoMoreInteractions(userEventService);
    }

    @Test
    void shouldReturnBadRequestForInvalidDto() throws Exception {
        // Given
        String invalidJson = """
            {
                "userId": "",
                "eventType": "USER_LOGIN"
            }
            """;

        // When & Then
        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Validation failed: [userId: userId must not be blank]"));

        verifyNoInteractions(userEventService);
    }

    @Test
    void shouldHandleServiceException() throws Exception {
        // Given
        UserEventDto eventDto = new UserEventDto(
                "user-123",
                "USER_LOGIN",
                Map.of("sessionId", "abc123")
        );

        doThrow(new RuntimeException("Kafka unavailable"))
                .when(userEventService).sendEvent(any(UserEventDto.class));

        // When & Then
        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isInternalServerError());

        verify(userEventService).sendEvent(eventDto);
    }

    @Test
    void shouldValidateEventType() throws Exception {
        // Given
        UserEventDto invalidDto = new UserEventDto(
                "user-123",
                "",
                Map.of("sessionId", "abc123")
        );

        // When & Then
        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Validation failed: [eventType: eventType must not be blank]"));

        verifyNoInteractions(userEventService);
    }
}
