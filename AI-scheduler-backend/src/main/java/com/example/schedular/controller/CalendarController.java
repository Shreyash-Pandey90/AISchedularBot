package com.example.schedular.controller;

import com.example.schedular.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @PostMapping("/send-invite")
    public void sendCalendarInvite(@RequestParam String candidateEmail, @RequestParam String recruiterEmail, @RequestParam String interviewTime) {
        calendarService.sendCalendarInvite(candidateEmail, recruiterEmail, interviewTime);
    }
}
