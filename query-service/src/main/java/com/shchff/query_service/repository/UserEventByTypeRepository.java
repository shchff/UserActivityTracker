package com.shchff.query_service.repository;

import com.shchff.query_service.model.UserEventByType;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserEventByTypeRepository extends CassandraRepository<UserEventByType, UUID>
{
    List<UserEventByType> findByKeyEventType(String eventType);
}
