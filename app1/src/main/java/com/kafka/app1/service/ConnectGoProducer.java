package com.kafka.app1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.app1.configs.KafkaTopicConfig;
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

}
