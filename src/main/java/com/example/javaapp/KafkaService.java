package com.example.javaapp;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final List<String> receivedMessages = new CopyOnWriteArrayList<>();
    private static final String TOPIC = "test-topic";

    public KafkaService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        kafkaTemplate.send(TOPIC, "[" + timestamp + "] " + message);
    }

    @KafkaListener(topics = TOPIC, groupId = "java-app-group")
    public void listen(String message) {
        System.out.println("Message reçu de Kafka: " + message);
        receivedMessages.add(0, message); // Ajouter au début pour voir les plus récents
        if (receivedMessages.size() > 50) {
            receivedMessages.remove(receivedMessages.size() - 1);
        }
    }

    public List<String> getReceivedMessages() {
        return receivedMessages;
    }

    public void clearMessages() {
        receivedMessages.clear();
    }
}
