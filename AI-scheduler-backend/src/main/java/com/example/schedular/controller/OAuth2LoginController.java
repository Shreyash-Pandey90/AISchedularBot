package com.example.schedular.controller;

import com.example.schedular.model.Candidate;
import com.example.schedular.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/candidates")
public class OAuth2LoginController {

    @Autowired
    private CandidateService candidateService;

    @GetMapping("/login-success")
    public ResponseEntity<?> loginSuccess(@AuthenticationPrincipal OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        // Check if the candidate already exists
        Candidate candidate = candidateService.getCandidateByEmail(email);
        if (candidate == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Candidate Does Not Exist");
        }

        return ResponseEntity.ok(candidate);
    }
}