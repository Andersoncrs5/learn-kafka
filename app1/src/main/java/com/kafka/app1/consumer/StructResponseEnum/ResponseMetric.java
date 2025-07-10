package com.kafka.app1.consumer.StructResponseEnum;

import com.kafka.app1.consumer.MetricEnum.ColumnTaskMetricEntity;
import com.kafka.app1.consumer.MetricEnum.SumOrRedEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ResponseMetric {
    private SumOrRedEnum action;
    private Long id;
    private ColumnTaskMetricEntity column;
}