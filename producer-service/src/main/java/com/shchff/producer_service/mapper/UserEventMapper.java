package com.shchff.producer_service.mapper;

import com.shchff.common.model.UserEvent;
import com.shchff.producer_service.dto.UserEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserEventMapper
{
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "timestamp", expression = "java(java.time.Instant.now())")
    UserEvent toEntity(UserEventDto dto);
}
