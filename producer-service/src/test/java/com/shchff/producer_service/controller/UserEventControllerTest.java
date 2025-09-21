package com.shchff.producer_service.controller;

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
        "logging.level.com.shchff.producer_service=DEBUG"
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

        // When & Then
        mockMvc.perform(post("/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ACCEPTED"));

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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.details[0].field").value("userId"))
                .andExpect(jsonPath("$.details[0].message").exists());

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
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Internal server error"));

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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.details[0].field").value("eventType"))
                .andExpect(jsonPath("$.details[0].message").exists());

        verifyNoInteractions(userEventService);
    }
}
