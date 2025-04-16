package com.example.kafka_demo.controller;
import com.example.kafka_demo.model.UserActivity;
import com.example.kafka_demo.service.UserActivityProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activities")
public class UserActivityController {
    private final UserActivityProducer producer;

    public UserActivityController(UserActivityProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public ResponseEntity<String> publishActivity(@RequestBody UserActivity activity) {
        producer.sendActivity(activity);
        return ResponseEntity.ok("Activity sent to Kafka");
    }
}
//https://cloud.redpanda.com/clusters
