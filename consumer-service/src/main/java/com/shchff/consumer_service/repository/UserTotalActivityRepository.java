package com.shchff.consumer_service.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserTotalActivityRepository
{
    private final CqlTemplate cqlTemplate;

    public void incrementCounter(String userId)
    {
        String INCREMENT_QUERY = "UPDATE user_total_activity SET event_count = event_count + 1 WHERE user_id = ?";
        cqlTemplate.execute(INCREMENT_QUERY, userId);
    }
}
