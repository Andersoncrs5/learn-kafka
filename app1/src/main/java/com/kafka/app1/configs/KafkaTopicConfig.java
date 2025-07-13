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
    public static final String TOPIC_PARTITION_KEY_SEND_MESSAGE = "test-study-partition-key-send-message";
    public static final String TOPIC_TOPICO_PROCESSAMENTO = "topico-processamento";
    public static final String TOPIC_TOPICO_PROCESSAMENTO_DLT = "topico-processamento.DLT";
    public static final String TOPIC_BATCH = "topic-batch";
    public static final String TOPIC_WITH_HEADER = "topic-header";
    public static final String TOPIC_BANK = "topic-bank";
    public static final String TOPIC_CONNECT_GO = "topic_connect_go";
    public static final String TOPIC_ORDER_GO = "topic_order_go";

    @Bean
    public NewTopic TopicSendObjGo() {
        return TopicBuilder.name(TOPIC_ORDER_GO)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic TopicConnectGo() {
        return TopicBuilder.name(TOPIC_CONNECT_GO)
                .partitions(5)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic TopicBank() {
        return TopicBuilder.name(TOPIC_BANK)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic TopicWithHeader() {
        return TopicBuilder.name(TOPIC_WITH_HEADER)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic SendTopicBatch() {
        return TopicBuilder.name(TOPIC_BATCH)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic SendMessgeDlt() {
        return TopicBuilder.name(TOPIC_TOPICO_PROCESSAMENTO_DLT)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic SendMessge() {
        return TopicBuilder.name(TOPIC_TOPICO_PROCESSAMENTO)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic PartitionSendMessge() {
        return TopicBuilder.name(TOPIC_PARTITION_SEND_MESSAGE)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic KeyTopic() {
        return TopicBuilder.name(TOPIC_PARTITION_KEY_SEND_MESSAGE)
                .partitions(4)
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
