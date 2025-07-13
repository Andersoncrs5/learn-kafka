package com.kafka.app1.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.app1.configs.KafkaTopicConfig;
import com.kafka.app1.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConnectGoProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public void helloWord() {
        kafkaTemplate.send(KafkaTopicConfig.TOPIC_CONNECT_GO, "hello word in kafka inside go");
    }

    public void sendOrder(Order order)  {
        try {
            String orderString = objectMapper.writeValueAsString(order);
            kafkaTemplate.send(KafkaTopicConfig.TOPIC_ORDER_GO, orderString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
