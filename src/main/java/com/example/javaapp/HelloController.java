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
        return "<!DOCTYPE html>\n" +
                "<html lang=\"fr\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Java App - Kafka Integration</title>\n" +
                "    <style>\n" +
                "        * { margin: 0; padding: 0; box-sizing: border-box; }\n" +
                "        body {\n" +
                "            font-family: 'Inter', system-ui, -apple-system, sans-serif;\n" +
                "            background: #0f172a;\n" +
                "            color: #f8fafc;\n" +
                "            min-height: 100vh;\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "            justify-content: center;\n" +
                "        }\n" +
                "        .container {\n" +
                "            background: #1e293b;\n" +
                "            padding: 40px;\n" +
                "            border-radius: 24px;\n" +
                "            box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);\n" +
                "            text-align: center;\n" +
                "            max-width: 800px;\n" +
                "            width: 90%;\n" +
                "            border: 1px solid #334155;\n" +
                "        }\n" +
                "        h1 { color: #38bdf8; font-size: 2.5em; margin-bottom: 10px; }\n" +
                "        .subtitle { color: #94a3b8; margin-bottom: 30px; }\n" +
                "        .actions {\n" +
                "            display: grid;\n" +
                "            grid-template-columns: 1fr 1fr;\n" +
                "            gap: 20px;\n" +
                "            margin-top: 30px;\n" +
                "        }\n" +
                "        .btn {\n" +
                "            padding: 16px 24px;\n" +
                "            border-radius: 12px;\n" +
                "            font-weight: 600;\n" +
                "            cursor: pointer;\n" +
                "            transition: all 0.2s;\n" +
                "            border: none;\n" +
                "            font-size: 1em;\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "            justify-content: center;\n" +
                "            gap: 10px;\n" +
                "        }\n" +
                "        .btn-send {\n" +
                "            background: #0ea5e9;\n" +
                "            color: white;\n" +
                "        }\n" +
                "        .btn-send:hover { background: #0284c7; transform: translateY(-2px); }\n" +
                "        .btn-read {\n" +
                "            background: #10b981;\n" +
                "            color: white;\n" +
                "        }\n" +
                "        .btn-read:hover { background: #059669; transform: translateY(-2px); }\n" +
                "        .message-box {\n" +
                "            margin-top: 30px;\n" +
                "            padding: 20px;\n" +
                "            background: #0f172a;\n" +
                "            border-radius: 12px;\n" +
                "            text-align: left;\n" +
                "            height: 300px;\n" +
                "            overflow-y: auto;\n" +
                "            border: 1px solid #334155;\n" +
                "            font-family: 'Fira Code', monospace;\n" +
                "            font-size: 0.9em;\n" +
                "        }\n" +
                "        .msg {\n" +
                "            padding: 8px 0;\n" +
                "            border-bottom: 1px solid #1e293b;\n" +
                "            color: #cbd5e1;\n" +
                "        }\n" +
                "        .status {\n" +
                "            margin-top: 15px;\n" +
                "            font-size: 0.8em;\n" +
                "            color: #64748b;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Kafka Demo App</h1>\n" +
                "        <p class=\"subtitle\">Flux de données temps réel sur K3s</p>\n" +
                "        \n" +
                "        <div class=\"actions\">\n" +
                "            <button class=\"btn btn-send\" onclick=\"sendData()\">\n" +
                "                🚀 Envoyer à Kafka (x100)\n" +
                "            </button>\n" +
                "            <button class=\"btn btn-read\" onclick=\"readData()\">\n" +
                "                📥 Lire de Kafka\n" +
                "            </button>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"message-box\" id=\"messageBox\">\n" +
                "            <div class=\"msg text-center\">En attente de données...</div>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"status\" id=\"status\">Prêt</div>\n" +
                "    </div>\n" +
                "\n" +
                "    <script>\n" +
                "        async function sendData() {\n" +
                "            document.getElementById('status').innerText = 'Envoi en cours...';\n" +
                "            try {\n" +
                "                const res = await fetch('/api/kafka/send', { method: 'POST' });\n" +
                "                if (res.ok) {\n" +
                "                    document.getElementById('status').innerText = '100 messages envoyés !';\n" +
                "                    setTimeout(() => readData(), 500);\n" +
                "                }\n" +
                "            } catch (e) {\n" +
                "                document.getElementById('status').innerText = 'Erreur d\\'envoi';\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        async function readData() {\n" +
                "            const box = document.getElementById('messageBox');\n" +
                "            try {\n" +
                "                const res = await fetch('/api/kafka/messages');\n" +
                "                const data = await res.json();\n" +
                "                if (data.length === 0) {\n" +
                "                    box.innerHTML = '<div class=\"msg\">Aucun message disponible.</div>';\n" +
                "                } else {\n" +
                "                    box.innerHTML = data.map(m => `<div class=\"msg\">${m}</div>`).join('');\n" +
                "                }\n" +
                "                document.getElementById('status').innerText = `Dernière lecture: ${new Date().toLocaleTimeString()} (${data.length} messages)`;\n" +
                "            } catch (e) {\n" +
                "                box.innerHTML = '<div class=\"msg text-red-500\">Erreur de lecture. Vérifiez la connexion.</div>';\n" +
                "            }\n" +
                "        }\n" +
                "        \n" +
                "        // Mise à jour auto toutes les 2s\n" +
                "        setInterval(readData, 2000);\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
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

