package com.example.schedular.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "candidates")
public class Candidate {

    @Id
    private String id;
    private String name;

    @Indexed(unique = true)
    private String email;
    private String password;
    private List<String> availableSlots; // Example: ["2025-03-20T10:00", "2025-03-21T15:00"]

    public Candidate() {}

    public Candidate(String name, String email, List<String> availableSlots,String password) {
        this.name = name;
        this.email = email;
        this.availableSlots = availableSlots;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(List<String> availableSlots) {
        this.availableSlots = availableSlots;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
