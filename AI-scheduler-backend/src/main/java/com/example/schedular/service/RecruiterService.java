package com.example.schedular.service;

import com.example.schedular.model.Candidate;
import com.example.schedular.model.Recruiter;
import com.example.schedular.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecruiterService {

    @Autowired
    private RecruiterRepository recruiterRepository;

    public Recruiter saveRecruiter(Recruiter recruiter) {
        return recruiterRepository.save(recruiter);
    }

    public Recruiter getRecruiterById(String id) {
        return recruiterRepository.findById(id).orElse(null);
    }

    public List<Recruiter> getAllRecruiters() {
        return recruiterRepository.findAll();
    }

    public void deleteRecruiter(String id) {
        recruiterRepository.deleteById(id);
    }

    public Recruiter loginRecruiter(String email, String password) {
        Optional<Recruiter> recruiterOpt = recruiterRepository.findByEmail(email);
        if (recruiterOpt.isPresent() && recruiterOpt.get().getPassword().equals(password)) {
            return recruiterOpt.get();
        }
        return null;
    }

    public Recruiter getRecruiterByEmail(String email) {
        return recruiterRepository.findByEmail(email).orElse(null);
    }
}
