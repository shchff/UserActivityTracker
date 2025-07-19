package com.shchff.consumer_service.repository;

import com.shchff.consumer_service.model.UserEventByType;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserEventByTypeRepository extends CassandraRepository<UserEventByType, UUID>
{
}
