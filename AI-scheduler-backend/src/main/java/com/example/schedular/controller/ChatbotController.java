package com.example.schedular.controller;

import com.example.schedular.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    @PostMapping("/message")
    public String processMessage(@RequestBody String message) {
        return chatbotService.processMessage(message);
    }
}
