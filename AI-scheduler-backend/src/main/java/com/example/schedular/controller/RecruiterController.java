package com.example.schedular.controller;

import com.example.schedular.model.Candidate;
import com.example.schedular.model.LoginRequest;
import com.example.schedular.model.Recruiter;
import com.example.schedular.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruiters")
public class RecruiterController {

    @Autowired
    private RecruiterService recruiterService;

    @PostMapping
    public Recruiter addRecruiter(@RequestBody Recruiter recruiter) {
        return recruiterService.saveRecruiter(recruiter);
    }

    @GetMapping("/{id}")
    public Recruiter getRecruiter(@PathVariable String id) {
        return recruiterService.getRecruiterById(id);
    }

    @GetMapping
    public List<Recruiter> getAllRecruiters() {
        return recruiterService.getAllRecruiters();
    }

    @DeleteMapping("/{id}")
    public String deleteRecruiter(@PathVariable String id) {
        recruiterService.deleteRecruiter(id);
        return "Recruiter deleted successfully!";
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginRecruiter(@RequestBody LoginRequest loginRequest) {
        Recruiter recruiter = recruiterService.loginRecruiter(loginRequest.getEmail(), loginRequest.getPassword());
        if (recruiter != null) {
            return ResponseEntity.ok(recruiter);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password!");
        }
    }
}
