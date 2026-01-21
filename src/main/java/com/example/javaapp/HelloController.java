package com.example.javaapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "<html><head><style>body { font-family: Arial, sans-serif; background-color: #f0f0f0; color: #333; text-align: center; padding: 50px; }</style></head><body><h1>Hello World from Java App!</h1><p>Welcome to your Java application.</p></body></html>";
    }
}
