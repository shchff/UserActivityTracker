package com.shchff.consumer_service.service;

import com.shchff.common.model.UserEvent;

public interface CassandraPersistenceService
{
    void save(UserEvent event);
}
