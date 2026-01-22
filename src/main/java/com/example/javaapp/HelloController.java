package com.example.javaapp;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
public class HelloController {

    private final KafkaService kafkaService;

    public HelloController(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @GetMapping("/")
    public String hello() {
        return "<!DOCTYPE html>\n<html lang=\"fr\">\n<head>\n    <meta charset=\"UTF-8\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n    <title>Java App - Pipeline CI/CD</title>\n    <style>\n        * {\n            margin: 0;\n            padding: 0;\n            box-sizing: border-box;\n        }\n        \n        body {\n            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n            min-height: 100vh;\n            display: flex;\n            align-items: center;\n            justify-content: center;\n            color: #333;\n        }\n        \n        .container {\n            background: rgba(255, 255, 255, 0.95);\n            padding: 60px;\n            border-radius: 20px;\n            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);\n            text-align: center;\n            max-width: 600px;\n            backdrop-filter: blur(10px);\n            border: 1px solid rgba(255, 255, 255, 0.2);\n        }\n        \n        h1 {\n            color: #667eea;\n            font-size: 3em;\n            margin-bottom: 20px;\n            font-weight: 700;\n            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);\n        }\n        \n        .subtitle {\n            color: #666;\n            font-size: 1.3em;\n            margin-bottom: 40px;\n            line-height: 1.6;\n        }\n        \n        .status {\n            display: inline-block;\n            background: linear-gradient(45deg, #4CAF50, #45a049);\n            color: white;\n            padding: 15px 30px;\n            border-radius: 50px;\n            font-weight: 600;\n            margin-bottom: 30px;\n            box-shadow: 0 4px 15px rgba(76, 175, 80, 0.3);\n            animation: pulse 2s infinite;\n        }\n        \n        @keyframes pulse {\n            0% { transform: scale(1); }\n            50% { transform: scale(1.05); }\n            100% { transform: scale(1); }\n        }\n        \n        .features {\n            display: grid;\n            grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));\n            gap: 20px;\n            margin-top: 40px;\n        }\n        \n        .feature {\n            background: linear-gradient(45deg, #f093fb, #f5576c);\n            color: white;\n            padding: 20px;\n            border-radius: 15px;\n            font-weight: 500;\n            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);\n            transition: transform 0.3s ease;\n        }\n        \n        .feature:hover {\n            transform: translateY(-5px);\n        }\n        \n        .java-logo {\n            font-size: 4em;\n            margin-bottom: 20px;\n            animation: bounce 2s infinite;\n        }\n        \n        @keyframes bounce {\n            0%, 20%, 50%, 80%, 100% { transform: translateY(0); }\n            40% { transform: translateY(-20px); }\n            60% { transform: translateY(-10px); }\n        }\n        \n        /* Kafka Buttons */\n        .kafka-actions {\n            margin-top: 40px;\n            display: grid;\n            grid-template-columns: 1fr 1fr;\n            gap: 15px;\n        }\n        \n        .btn {\n            padding: 12px 20px;\n            border-radius: 10px;\n            border: none;\n            color: white;\n            font-weight: bold;\n            cursor: pointer;\n            transition: background 0.3s;\n        }\n        \n        .btn-kafka-send { background: #667eea; }\n        .btn-kafka-send:hover { background: #5a6fd6; }\n        .btn-kafka-read { background: #f5576c; }\n        .btn-kafka-read:hover { background: #e44d5f; }\n\n        .kafka-logs {\n            margin-top: 20px;\n            padding: 10px;\n            background: #f8f9fa;\n            border-radius: 8px;\n            font-family: monospace;\n            font-size: 0.8em;\n            max-height: 150px;\n            overflow-y: auto;\n            text-align: left;\n            border: 1px solid #ddd;\n        }\n        \n        .footer {\n            margin-top: 40px;\n            color: #888;\n            font-size: 0.9em;\n        }\n    </style>\n</head>\n<body>\n    <div class=\"container\">\n        <div class=\"java-logo\">☕</div>\n        <h1>Java App</h1>\n        <p class=\"subtitle\">Application de démonstration pour pipeline CI/CD</p>\n        \n        <div class=\"status\">\n            ✅ Application en ligne\n        </div>\n        \n        <div class=\"features\">\n            <div class=\"feature\">🚀 Spring Boot</div>\n            <div class=\"feature\">🔄 CI/CD Pipeline</div>\n            <div class=\"feature\">🐳 Docker Ready</div>\n            <div class=\"feature\">☕ Java 17+</div>\n        </div>\n\n        <div class=\"kafka-actions\">\n            <button class=\"btn btn-kafka-send\" onclick=\"sendKafka()\">Envoyer à Kafka</button>\n            <button class=\"btn btn-kafka-read\" onclick=\"readKafka()\">Lire de Kafka</button>\n        </div>\n        \n        <div id=\"kafkaLogs\" class=\"kafka-logs\">Logs Kafka...</div>\n        \n        <div class=\"footer\">\n            <p>Déployé avec succès via votre pipeline d'intégration continue</p>\n        </div>\n    </div>\n\n    <script>\n        async function sendKafka() {\n            await fetch('api/kafka/send', { method: 'POST' });\n            document.getElementById('kafkaLogs').innerText = '100 messages envoyés !';\n        }\n\n        async function readKafka() {\n            const res = await fetch('api/kafka/messages');\n            const data = await res.json();\n            const logBox = document.getElementById('kafkaLogs');\n            if (data.length === 0) {\n                logBox.innerText = 'Aucun message.';\n            } else {\n                logBox.innerHTML = data.map(m => `<div>${m}</div>`).join('');\n            }\n        }\n        \n        // Refresh auto\n        setInterval(readKafka, 3000);\n    </script>\n</body>\n</html>";
    }

    @PostMapping("/api/kafka/send")
    public String send() {
        for (int i = 0; i < 100; i++) {
            kafkaService.sendMessage("Data Log #" + i + " - " + UUID.randomUUID().toString().substring(0, 8));
        }
        return "100 messages sent to Kafka";
    }

    @GetMapping("/api/kafka/messages")
    public List<String> getMessages() {
        return kafkaService.getReceivedMessages();
    }

    @PostMapping("/api/kafka/clear")
    public String clear() {
        kafkaService.clearMessages();
        return "Messages cleared";
    }
}

