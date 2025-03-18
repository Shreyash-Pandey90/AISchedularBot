package com.example.schedular.service;

import com.example.schedular.model.InterviewSchedule;
import com.example.schedular.repository.InterviewScheduleRepository;
import com.example.schedular.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class SchedulingService {

    @Autowired
    private InterviewScheduleRepository scheduleRepository;

    @Autowired
    private CalendarService calendarService;

    public InterviewSchedule scheduleInterview(String candidateEmail, String recruiterEmail, String interviewTime) {
        // Save schedule in DB
        InterviewSchedule schedule = new InterviewSchedule();

        schedule.setId(UUID.randomUUID().toString());
        schedule.setCandidateEmail(candidateEmail);
        schedule.setRecruiterEmail(recruiterEmail);
        schedule.setInterviewTime(LocalDateTime.parse(interviewTime));
        InterviewSchedule savedSchedule = scheduleRepository.save(schedule);

        // Send Google Calendar Invite
        calendarService.sendCalendarInvite(candidateEmail, recruiterEmail, interviewTime);

        return savedSchedule;
    }

    public List<InterviewSchedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<InterviewSchedule> getSchedulesByCandidateEmail(String candidateEmail) {
        return scheduleRepository.findByCandidateEmail(candidateEmail);
    }

    public List<InterviewSchedule> getSchedulesByRecruiterEmail(String recruiterEmail) {
        return scheduleRepository.findByRecruiterEmail(recruiterEmail);
    }

    public void deleteSchedule(String id) {
        scheduleRepository.deleteById(id);
    }
}
