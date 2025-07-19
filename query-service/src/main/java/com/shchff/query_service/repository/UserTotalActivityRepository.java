package com.shchff.query_service.repository;

import com.shchff.query_service.model.UserTotalActivity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTotalActivityRepository extends CassandraRepository<UserTotalActivity, String>
{

}
