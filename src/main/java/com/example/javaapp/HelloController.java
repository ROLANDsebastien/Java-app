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
        return "<!DOCTYPE html>\n<html lang=\"fr\">\n<head>\n    <meta charset=\"UTF-8\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n    <title>Java App - Kafka Integration</title>\n    <style>\n        * { margin: 0; padding: 0; box-sizing: border-box; }\n        body {\n            font-family: 'Segoe UI', system-ui, sans-serif;\n            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n            min-height: 100vh;\n            display: flex;\n            align-items: center;\n            justify-content: center;\n            color: #333;\n        }\n        .container {\n            background: rgba(255, 255, 255, 0.95);\n            padding: 40px;\n            border-radius: 20px;\n            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);\n            text-align: center;\n            max-width: 1000px;\n            width: 95%;\n            backdrop-filter: blur(10px);\n        }\n        h1 { color: #667eea; font-size: 3.5em; margin-bottom: 10px; }\n        .subtitle { color: #666; margin-bottom: 30px; }\n        .features {\n            display: grid;\n            grid-template-columns: repeat(4, 1fr);\n            gap: 15px;\n            margin-bottom: 30px;\n        }\n        .feature {\n            background: linear-gradient(45deg, #f093fb, #f5576c);\n            color: white;\n            padding: 15px;\n            border-radius: 12px;\n            font-weight: 500;\n            font-size: 0.9em;\n        }\n        .kafka-box {\n            border-top: 1px solid #eee;\n            padding-top: 30px;\n        }\n        .input-group {\n            margin-bottom: 20px;\n            display: flex;\n            justify-content: center;\n            gap: 10px;\n            align-items: center;\n        }\n        .input-group input {\n            padding: 8px;\n            border-radius: 8px;\n            border: 1px solid #ddd;\n            width: 100px;\n            text-align: center;\n            font-weight: bold;\n        }\n        .btn-group {\n            display: grid;\n            grid-template-columns: repeat(3, 1fr);\n            gap: 15px;\n            margin-bottom: 20px;\n        }\n        .btn {\n            padding: 15px;\n            border-radius: 12px;\n            border: none;\n            color: white;\n            font-weight: bold;\n            cursor: pointer;\n            transition: transform 0.2s;\n        }\n        .btn:hover { transform: translateY(-3px); filter: brightness(1.1); }\n        .btn-send { background: #667eea; }\n        .btn-read { background: #f5576c; }\n        .btn-clear { background: #94a3b8; }\n        .logs {\n            background: #1e1e1e;\n            color: #d4d4d4;\n            padding: 20px;\n            border-radius: 12px;\n            text-align: left;\n            font-family: 'Consolas', monospace;\n            height: 200px;\n            overflow-y: auto;\n            font-size: 0.9em;\n            border: 2px solid #333;\n        }\n        .log-line { border-bottom: 1px solid #333; padding: 4px 0; }\n        .log-err { color: #f87171; }\n        .log-ok { color: #4ade80; }\n    </style>\n</head>\n<body>\n    <div class=\"container\">\n        <h1>☕ Java App</h1>\n        <p class=\"subtitle\">Dashboard Kafka & CI/CD Pipeline</p>\n        \n        <div class=\"features\">\n            <div class=\"feature\">🚀 Spring Boot</div>\n            <div class=\"feature\">🔄 CI/CD GitOps</div>\n            <div class=\"feature\">🐳 Docker Build</div>\n            <div class=\"feature\">⚙️ K3s Cluster</div>\n        </div>\n\n        <div class=\"kafka-box\">\n            <div class=\"input-group\">\n                <label>Nombre de messages :</label>\n                <input type=\"number\" id=\"msgCount\" value=\"100\">\n            </div>\n            <div class=\"btn-group\">\n                <button class=\"btn btn-send\" onclick=\"sendMsg()\">🚀 Envoyer</button>\n                <button class=\"btn btn-read\" onclick=\"readMsg()\">📥 Lire</button>\n                <button class=\"btn btn-clear\" onclick=\"clearLogs()\">🗑️ Vider</button>\n            </div>\n            <div id=\"logDisplay\" class=\"logs\">En attente d'action...</div>\n        </div>\n    </div>\n\n    <script>\n        const logDisplay = document.getElementById('logDisplay');\n\n        function addLog(msg, type = '') {\n            const div = document.createElement('div');\n            div.className = 'log-line ' + (type ? 'log-' + type : '');\n            div.innerText = `[${new Date().toLocaleTimeString()}] ${msg}`;\n            logDisplay.insertBefore(div, logDisplay.firstChild);\n        }\n\n        async function sendMsg() {\n            const count = document.getElementById('msgCount').value;\n            addLog(`Envoi de ${count} messages...`);\n            try {\n                const res = await fetch(`api/kafka/send?count=${count}`, { method: 'POST' });\n                if (res.ok) {\n                    addLog(`Succès ! ${count} messages envoyés.`, 'ok');\n                    setTimeout(readMsg, 1000);\n                } else {\n                    addLog(`Erreur Serveur: ${res.status}`, 'err');\n                }\n            } catch (e) {\n                addLog(`Erreur Réseau: ${e.message}`, 'err');\n            }\n        }\n\n        async function readMsg() {\n            try {\n                const res = await fetch('api/kafka/messages');\n                if (!res.ok) throw new Error('Status ' + res.status);\n                const data = await res.json();\n                if (data.length === 0) {\n                    addLog('Kafka est vide (ou pas encore synchronisé).');\n                } else {\n                    logDisplay.innerHTML = ''; // Reset pour l'affichage complet\n                    data.forEach(m => {\n                        const div = document.createElement('div');\n                        div.className = 'log-line';\n                        div.innerText = m;\n                        logDisplay.appendChild(div);\n                    });\n                    addLog(`Lecture de ${data.length} messages.`, 'ok');\n                }\n            } catch (e) {\n                addLog(`Erreur de lecture: ${e.message}`, 'err');\n            }\n        }\n\n        async function clearLogs() {\n            try {\n                await fetch('api/kafka/clear', { method: 'POST' });\n                logDisplay.innerHTML = '<div class=\"log-line\">Logs vidés sur le serveur.</div>';\n            } catch (e) {\n                addLog(`Erreur clear: ${e.message}`, 'err');\n            }\n        }\n\n        // Refresh auto toutes les 5s\n        setInterval(readMsg, 5000);\n        readMsg(); // Premier chargement\n    </script>\n</body>\n</html>";

    }

    @PostMapping("/api/kafka/send")
    public String send(@RequestParam(defaultValue = "100") int count) {
        for (int i = 0; i < count; i++) {
            kafkaService.sendMessage("Log Data #" + (i+1) + " - " + UUID.randomUUID().toString().substring(0, 8));
        }
        return count + " messages sent to Kafka";
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


