package com.example.schedular.controller;

import com.example.schedular.model.InterviewSchedule;
import com.example.schedular.service.SchedulingService;
import com.example.schedular.service.CalendarService;
import com.example.schedular.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private SchedulingService schedulingService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private ChatbotService chatbotService;

    @PostMapping
    public InterviewSchedule createSchedule(@RequestParam String candidateEmail,
                                            @RequestParam String recruiterEmail,
                                            @RequestParam String interviewTime) throws GeneralSecurityException, IOException {
        InterviewSchedule savedSchedule = schedulingService.scheduleInterview(candidateEmail, recruiterEmail, interviewTime);
        calendarService.sendCalendarInvite(candidateEmail, recruiterEmail, interviewTime); // Sync with Google Calendar
        return savedSchedule;
    }

    @GetMapping
    public List<InterviewSchedule> getAllSchedules() {
        return schedulingService.getAllSchedules();
    }

    @GetMapping("/candidate")
    public List<InterviewSchedule> getSchedulesForCandidate(@RequestParam String email) {
        return schedulingService.getSchedulesByCandidateEmail(email);
    }

    @GetMapping("/recruiter")
    public List<InterviewSchedule> getSchedulesForRecruiter(@RequestParam String email) {
        return schedulingService.getSchedulesByRecruiterEmail(email);
    }

}
