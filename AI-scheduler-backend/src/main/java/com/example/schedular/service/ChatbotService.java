package com.example.schedular.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.regex.*;

@Service
public class ChatbotService {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "YOUR_OPENAI_API_KEY";  // Replace with actual key

    public String processMessage(String message) {
        // Extract availability using regex-based NLP
        String extractedTime = extractDateTime(message);

        if (extractedTime != null) {
            return "Got it! Scheduling an interview at " + extractedTime;
        }

        // If regex fails, use AI model
        return getAIResponse(message);
    }

    private String extractDateTime(String message) {
        Pattern pattern = Pattern.compile("(\\bMonday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday\\b)\\s*(at|on)?\\s*(\\d{1,2}(:\\d{2})?\\s*(AM|PM)?)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            return matcher.group(1) + " at " + matcher.group(3);
        }
        return null;
    }

    private String getAIResponse(String message) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{ \"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"system\", \"content\": \"You are an interview scheduling assistant.\"}, {\"role\": \"user\", \"content\": \"" + message + "\"}]}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(OPENAI_API_URL, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }
}
