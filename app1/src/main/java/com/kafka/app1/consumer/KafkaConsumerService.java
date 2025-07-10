package com.kafka.app1.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.app1.configs.KafkaTopicConfig;
import com.kafka.app1.consumer.MetricEnum.ColumnTaskMetricEntity;
import com.kafka.app1.consumer.MetricEnum.SumOrRedEnum;
import com.kafka.app1.consumer.StructResponseEnum.ResponseMetric;
import com.kafka.app1.entity.TaskEntity;
import com.kafka.app1.repositories.TaskRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class KafkaConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerService.class);
    @Autowired
    private TaskRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = KafkaTopicConfig.TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMessage(String message) {
        LOGGER.info("Mensagem recebida do tópico '{}': '{}'", KafkaTopicConfig.TOPIC, message);
    }

    @KafkaListener(topics = KafkaTopicConfig.TOPIC_SEND_MESSAGE, groupId = "${spring.kafka.consumer.group-id}")
    public void sendMessage(String message) {
        LOGGER.info("Mensagem recebida do tópico: " + KafkaTopicConfig.TOPIC_SEND_MESSAGE + " is: " + message);
    }

    @KafkaListener(topics = KafkaTopicConfig.TOPIC_CHANGE_STATUS_TASK, groupId = "${spring.kafka.consumer.group-id}")
    public void changeStatus(String id) throws Exception {
        Optional<TaskEntity> optionalTask = this.repository.findById(Long.valueOf(id));

        if (optionalTask.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        TaskEntity task = optionalTask.get();

        task.isDone = !task.isDone;

        this.repository.save(task);
    }

    @KafkaListener(topics = KafkaTopicConfig.TOPIC_CREATE_NEW_TASK, groupId = "${spring.kafka.consumer.group-id}")
    public void createNewTask(String taskObj) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(taskObj);
        TaskEntity task = objectMapper.convertValue(node, TaskEntity.class);
        var result = this.repository.save(task);
        System.out.println(result.toString());
    }

    @KafkaListener(
            topicPartitions = @TopicPartition(
                    topic = KafkaTopicConfig.TOPIC_PARTITION_SEND_MESSAGE,
                    partitions = { "2" }
            ),

            groupId = "${spring.kafka.consumer.group-id}",
            concurrency = "3"
    )
    public void partition2SendMessage(String message) {
        System.out.println("\nMessage partition 2 is: " + message);
    }

    @KafkaListener(
            topicPartitions = @TopicPartition(
                    topic = KafkaTopicConfig.TOPIC_PARTITION_SEND_MESSAGE,
                    partitions = { "1" }
            ),
            groupId = "${spring.kafka.consumer.group-id}",
            concurrency = "3"
    )
    public void partition1SendMessage(String message) {System.out.println("\nMessage partition 1 is: " + message);}

    @KafkaListener(
            topicPartitions = @TopicPartition(
                    topic = KafkaTopicConfig.TOPIC_PARTITION_SEND_MESSAGE,
                    partitions = { "0" }
            ),
            groupId = "${spring.kafka.consumer.group-id}",
            concurrency = "3"
    )
    public void partition0SendMessage(String message) {System.out.println("\nMessage partition 0 is: " + message);}

    @KafkaListener(topics = KafkaTopicConfig.TOPIC_SUM_RED_METRIC_TASK, groupId = "${spring.kafka.consumer.group-id}")
    public void sumOrRedTaskMetric(String metricObj) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(metricObj);
        ResponseMetric metric = objectMapper.convertValue(node, ResponseMetric.class);

        Optional<TaskEntity> optionalTask = this.repository.findById(metric.getId());

        if (optionalTask.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        TaskEntity task = optionalTask.get();

        if (metric.getColumn() == ColumnTaskMetricEntity.EDIT_COUNT && metric.getAction() == SumOrRedEnum.SUM){
            task.editCount += 1;
        }

        if (metric.getColumn() == ColumnTaskMetricEntity.EDIT_COUNT && metric.getAction() == SumOrRedEnum.RED){
            task.editCount -= 1;
        }

        if (metric.getColumn() == ColumnTaskMetricEntity.TIMES_COUNT && metric.getAction() == SumOrRedEnum.SUM){
            task.timesCount += 1;
        }

        if (metric.getColumn() == ColumnTaskMetricEntity.TIMES_COUNT && metric.getAction() == SumOrRedEnum.RED){
            task.timesCount -= 1;
        }

        this.repository.save(task);

    }

    @KafkaListener(topics = KafkaTopicConfig.TOPIC_PARTITION_KEY_SEND_MESSAGE, groupId = "${spring.kafka.consumer.group-id}")
    public void withChave(ConsumerRecord<String, String> record) {
        System.out.printf("[key=%s] valor=%s | partição=%d%n", record.key(), record.value(), record.partition());
    }



}
