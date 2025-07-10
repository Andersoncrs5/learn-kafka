package com.kafka.app1.controller;

import com.kafka.app1.consumer.KafkaConsumerService;
import com.kafka.app1.consumer.MetricEnum.ColumnTaskMetricEntity;
import com.kafka.app1.consumer.MetricEnum.SumOrRedEnum;
import com.kafka.app1.entity.TaskEntity;
import com.kafka.app1.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hello")
public class HelloController {

    @Autowired
    private HelloService helloService;
    @Autowired
    private KafkaConsumerService consumerService;

    @GetMapping("/")
    public ResponseEntity<String> getHello() {
        String message = helloService.getHelloMessage();
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{message}")
    public ResponseEntity<String> sendMessage(@PathVariable String message) {
        helloService.sendMesage(message);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/changeStatus/{id}")
    public ResponseEntity<String> changeStatus(@PathVariable Long id) {
        helloService.changeStatus(id);
        return ResponseEntity.ok("Change");
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody TaskEntity task) {
        var result = this.helloService.save(task);
        return ResponseEntity.ok(result);
    }

    @PostMapping("create-kafka/")
    public ResponseEntity<?> createKafka(@RequestBody TaskEntity task) {
        this.helloService.saveInKafka(task);
        return ResponseEntity.ok("Mandando to kafka");
    }

    @GetMapping("get/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        var result = this.helloService.get(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> changeStatus() {
        return ResponseEntity.ok(helloService.getAll());
    }

    @GetMapping("get/{id}/{action}/{column}")
    public ResponseEntity<?> sumOrRedMetric(@PathVariable Long id, @PathVariable SumOrRedEnum action, @PathVariable ColumnTaskMetricEntity column) {
        var result = this.helloService.get(id);
        this.helloService.sumOrRedMetric(result, action, column);
        return ResponseEntity.ok(this.helloService.get(id));
    }

    @GetMapping("send-message-partition-0/{message}")
    public ResponseEntity<String> sendMessagePartition0(@PathVariable String message) {
        consumerService.partition0SendMessage(message);
        return ResponseEntity.ok(message);
    }

    @GetMapping("send-message-partition-1/{message}")
    public ResponseEntity<String> sendMessagePartition1(@PathVariable String message) {
        consumerService.partition1SendMessage(message);
        return ResponseEntity.ok(message);
    }

}