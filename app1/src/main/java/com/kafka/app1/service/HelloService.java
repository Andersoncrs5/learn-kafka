package com.kafka.app1.service;

import com.kafka.app1.consumer.MetricEnum.ColumnTaskMetricEntity;
import com.kafka.app1.consumer.MetricEnum.SumOrRedEnum;
import com.kafka.app1.consumer.StructResponseEnum.ResponseMetric;
import com.kafka.app1.entity.TaskEntity;
import com.kafka.app1.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class HelloService {

    @Autowired
    private TaskRepository repository;
    @Autowired
    private KafkaProducerService kafkaProducerService;

    public void sendMessageWithHeader(String username, String message) {
        kafkaProducerService.sendMesageWithTopic(username, message);
    }

    public String getHelloMessage() {
        String message = "Hello World from Spring Boot Microservice with Kafka!";
        kafkaProducerService.sendMessage(message);
        return message;
    }

    public void changeStatus(Long id) {
        kafkaProducerService.sendChangeStatus(id.toString());
    }

    public void sendMesage(String message) {
        kafkaProducerService.sendMessage1(message);
    }

    public void sendMesageWithCallBack(String message) {
        kafkaProducerService.sendMessageWithCallBack(message);
    }

    public List<TaskEntity> getAll() {
        return this.repository.findAll();
    }

    public TaskEntity get(Long id) {
        Optional<TaskEntity> task = this.repository.findById(id);

        if (task.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return task.get();
    }

    public TaskEntity save(TaskEntity task) {
        return this.repository.save(task);
    }

    public void saveInKafka(TaskEntity task) {
        kafkaProducerService.createNewTask(task);
    }

    public void sumOrRedMetric(TaskEntity task, SumOrRedEnum action, ColumnTaskMetricEntity column) {
        ResponseMetric metric = new ResponseMetric(action, task.id, column);
        kafkaProducerService.sumOrRedMetricTask(metric);
    }

    public void sendMessageDql(String message) {
        kafkaProducerService.process(message);
    }

    public void sendLotes(int amount) {
        kafkaProducerService.sendInLotes(amount);
    }

    public void transferir(String origem, String destino, double valor) {
        kafkaProducerService.transferir(origem, destino, valor);
    }
}
