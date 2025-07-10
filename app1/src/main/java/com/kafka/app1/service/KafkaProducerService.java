package com.kafka.app1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.app1.configs.KafkaTopicConfig;
import com.kafka.app1.consumer.StructResponseEnum.ResponseMetric;
import com.kafka.app1.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public void sendMessageWithCallBack(String message){
        kafkaTemplate.send(KafkaTopicConfig.TOPIC, message).whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("✅ Enviado com sucesso! Topic: " + result.getRecordMetadata().topic() +
                        ", Partition: " + result.getRecordMetadata().partition() +
                        ", Offset: " + result.getRecordMetadata().offset());
            } else {
                System.err.println("❌ Falha ao enviar: " + ex.getMessage());
            }
        });
    }

    public void sendMessagePartition0(String message) {
        kafkaTemplate.send(KafkaTopicConfig.TOPIC_PARTITION_SEND_MESSAGE, 0, "1","Using partition 0");
    }

    public void sendMessagePartition1(String message) {
        kafkaTemplate.send(KafkaTopicConfig.TOPIC_PARTITION_SEND_MESSAGE, 1, "2","Using partition 1");
    }

    public void sendMessage(String message){
        kafkaTemplate.send(KafkaTopicConfig.TOPIC, message);
    }



    public void sendMessage1(String message) {
        kafkaTemplate.send(KafkaTopicConfig.TOPIC_SEND_MESSAGE, message);
    }

    public void sendChangeStatus(String id) {
        if (id.isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id is required");
        kafkaTemplate.send(KafkaTopicConfig.TOPIC_CHANGE_STATUS_TASK, id);
    }

    public void createNewTask(TaskEntity task) {
        try {
            String taskMappear = objectMapper.writeValueAsString(task);
            kafkaTemplate.send(KafkaTopicConfig.TOPIC_CREATE_NEW_TASK, taskMappear);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error the create new task in kafka");
        }
    }

    public void sumOrRedMetricTask(ResponseMetric metric) {
        try {
            String mapper = objectMapper.writeValueAsString(metric);
            kafkaTemplate.send(KafkaTopicConfig.TOPIC_SUM_RED_METRIC_TASK, mapper);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error the create new task in kafka");
        }
    }

    public void withKey(String chave, String valor) {
        kafkaTemplate.send(KafkaTopicConfig.TOPIC_PARTITION_KEY_SEND_MESSAGE, chave, valor);
    }

}
