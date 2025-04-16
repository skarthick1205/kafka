package com.example.kafka_demo.consumer;

import com.example.kafka_demo.model.UserActivity;
import com.example.kafka_demo.repository.UserActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class UserActivityConsumer {
    private final UserActivityRepository repository;

    public UserActivityConsumer(UserActivityRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "user-activity-topic", groupId = "user-activity-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(UserActivity activity) {
        repository.save(activity);
        System.out.println("Consumed and saved: " + activity);
    }
}
