package com.kafka.app1.configs;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class LoggingProducerInterceptor implements ProducerInterceptor<String, String> {
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        System.out.println("📤 Enviando: " + record.value() + " para " + record.topic());
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        System.out.println("✅ Enviado com sucesso para partição: " + metadata.partition());
    }

    @Override
    public void close() {}

    @Override
    public void configure(Map<String, ?> configs) {}
}
