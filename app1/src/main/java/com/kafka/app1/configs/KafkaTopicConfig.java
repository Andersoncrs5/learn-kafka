package com.kafka.app1.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.config.TopicBuilder;


@Configuration
public class KafkaTopicConfig {
    public static final String TOPIC = "test1-study-kafka";
    public static final String TOPIC_SEND_MESSAGE = "test1-study-kafka-send-message";
    public static final String TOPIC_CHANGE_STATUS_TASK = "test1-study-kafka-change-status";
    public static final String TOPIC_CREATE_NEW_TASK = "test1-study-kafka-create-task";
    public static final String TOPIC_SUM_RED_METRIC_TASK = "test1-study-kafka-sum-red-metric-task";
    public static final String TOPIC_PARTITION_SEND_MESSAGE = "test-study-partition-send-message";

    @Bean
    public NewTopic PartitionSendMessge() {
        return TopicBuilder.name(TOPIC_PARTITION_SEND_MESSAGE)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic SumOrRedMetricTaskTopic() {
        return TopicBuilder.name(TOPIC_SUM_RED_METRIC_TASK)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic CreateTaskTopic() {
        return TopicBuilder.name(TOPIC_CREATE_NEW_TASK)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic Test1Topic() {
        return TopicBuilder.name(TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic SendMessage() {
        return TopicBuilder.name(TOPIC_SEND_MESSAGE)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic ChangeStatus() {
        return TopicBuilder.name(TOPIC_CHANGE_STATUS_TASK)
                .partitions(1)
                .replicas(1)
                .build();
    }

}
