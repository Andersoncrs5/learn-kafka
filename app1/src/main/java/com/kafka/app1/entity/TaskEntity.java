package com.kafka.app1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TaskEntity {
    @Id
//    @GeneratedValue(strategy = )
    public Long id = 0L;

    public String title = "";

    public String description = "";

    public Boolean isDone = false;

    public Long editCount = 0L;

    public Long timesCount = 0L;

}
