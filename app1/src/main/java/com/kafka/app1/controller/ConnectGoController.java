package com.kafka.app1.controller;

import com.kafka.app1.service.ConnectGoProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/golang")
public class ConnectGoController {

    @Autowired
    private ConnectGoProducer producer;

    @GetMapping("/HelloWorld")
    public ResponseEntity<?> helloWorld() {
        producer.helloWord();
        return ResponseEntity.ok("Message sended!");
    }

}