package com.example.kafka_demo.service;

import com.example.kafka_demo.model.UserActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class UserActivityProducer {
    private final KafkaTemplate<String, UserActivity> kafkaTemplate;

    public UserActivityProducer(KafkaTemplate<String, UserActivity> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendActivity(UserActivity activity) {
        kafkaTemplate.send("user-activity-topic", activity);
    }
}

