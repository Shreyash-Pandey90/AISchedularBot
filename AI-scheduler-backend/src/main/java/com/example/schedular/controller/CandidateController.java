package com.example.schedular.controller;

import com.example.schedular.model.Candidate;
import com.example.schedular.model.LoginRequest;
import com.example.schedular.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @PostMapping
    public Candidate addCandidate(@RequestBody Candidate candidate) {
        return candidateService.saveCandidate(candidate);
    }

    @GetMapping("/{id}")
    public Candidate getCandidate(@PathVariable String id) {
        return candidateService.getCandidateById(id);
    }

    @GetMapping
    public List<Candidate> getAllCandidates() {
        return candidateService.getAllCandidates();
    }

    @DeleteMapping("/{id}")
    public String deleteCandidate(@PathVariable String id) {
        candidateService.deleteCandidate(id);
        return "Candidate deleted successfully!";
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginCandidate(@RequestBody LoginRequest loginRequest) {
        Candidate candidate = candidateService.loginCandidate(loginRequest.getEmail(), loginRequest.getPassword());
        if (candidate != null) {
            return ResponseEntity.ok(candidate);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password!");
        }
    }

}
