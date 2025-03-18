package com.example.schedular.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "interview_schedules")
public class InterviewSchedule {
    
    @Id
    private String id;
    private String candidateEmail;
    private String recruiterEmail;
    private LocalDateTime interviewTime;
    
    public InterviewSchedule() {}

    public InterviewSchedule(String candidateEmail, String recruiterEmail, LocalDateTime interviewTime) {
        this.candidateEmail = candidateEmail;
        this.recruiterEmail = recruiterEmail;
        this.interviewTime = interviewTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    public String getRecruiterEmail() {
        return recruiterEmail;
    }

    public void setRecruiterEmail(String recruiterEmail) {
        this.recruiterEmail = recruiterEmail;
    }

    public LocalDateTime getInterviewTime() {
        return interviewTime;
    }

    public void setInterviewTime(LocalDateTime interviewTime) {
        this.interviewTime = interviewTime;
    }
}
