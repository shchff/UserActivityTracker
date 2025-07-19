package com.shchff.consumer_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("user_total_activity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTotalActivity
{
    @PrimaryKey
    @Column("user_id")
    private String userId;

    @Column("event_count")
    @CassandraType(type = CassandraType.Name.COUNTER)
    private Long eventCount;
}
