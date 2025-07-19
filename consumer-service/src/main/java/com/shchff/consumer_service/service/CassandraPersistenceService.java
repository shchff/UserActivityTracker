package com.shchff.consumer_service.service;

import com.shchff.consumer_service.model.UserEvent;

public interface CassandraPersistenceService
{
    void save(UserEvent event);
}
