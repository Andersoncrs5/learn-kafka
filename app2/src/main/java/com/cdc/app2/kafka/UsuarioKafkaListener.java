package com.cdc.app2.kafka;

import com.cdc.app2.model.Usuario;
import com.cdc.app2.service.UsuarioService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UsuarioKafkaListener {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "dbserver1.public.usuarios", groupId = "${spring.kafka.consumer.group-id}")
    public void processar(ConsumerRecord<String, String> record) {
        try {
            JsonNode root = objectMapper.readTree(record.value());

            JsonNode after = root.path("payload").path("after");

            if (!after.isNull() && !after.isMissingNode()) {
                Integer id = after.path("id").asInt();
                String name = after.path("name").asText();
                String email = after.path("email").asText();

                Usuario usuario = new Usuario(id, name, email);
                usuarioService.salvarOuAtualizar(usuario);
                System.out.println("✅ Usuario processado: " + name);
            } else {
                System.out.println("⚠️ Payload vazio (DELETE?)");
            }

        } catch (Exception e) {
            System.err.println("❌ Erro ao processar mensagem: " + e.getMessage());
        }
    }
}