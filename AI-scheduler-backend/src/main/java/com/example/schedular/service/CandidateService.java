package com.example.schedular.service;

import com.example.schedular.model.Candidate;
import com.example.schedular.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    public Candidate saveCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public Candidate getCandidateById(String id) {
        return candidateRepository.findById(id).orElse(null);
    }

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public void deleteCandidate(String id) {
        candidateRepository.deleteById(id);
    }

    public Candidate loginCandidate(String email,String password) {
        Optional<Candidate> candidateOpt = candidateRepository.findByEmail(email);
        if (candidateOpt.isPresent() && candidateOpt.get().getPassword().equals(password)) {
            return candidateOpt.get();
        }
        return null;
    }

    public Candidate getCandidateByEmail(String email) {
        return candidateRepository.findByEmail(email).orElse(null);
    }
}
