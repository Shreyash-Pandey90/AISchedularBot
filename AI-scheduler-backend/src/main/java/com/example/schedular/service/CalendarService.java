package com.example.schedular.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
public class CalendarService {

    private static final String APPLICATION_NAME = "Interview Scheduler";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private Calendar getCalendarService() throws Exception {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        // Load credentials.json from resources
        InputStreamReader in = new InputStreamReader(getClass().getResourceAsStream("/credentials.json"));
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, in);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, Collections.singletonList("https://www.googleapis.com/auth/calendar"))
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        Credential credential = flow.loadCredential("user");
        return new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public void sendCalendarInvite(String candidateEmail, String recruiterEmail, String interviewTime) {
        try {
            Calendar service = getCalendarService();

            Event event = new Event()
                    .setSummary("Interview Scheduled")
                    .setDescription("Interview between candidate and recruiter");

            // Convert String time to Google EventDateTime
            Date date = new Date(interviewTime);
            EventDateTime start = new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(date))
                    .setTimeZone(TimeZone.getDefault().getID());
            event.setStart(start);
            event.setEnd(start);

            //have to implement how should I have to do?

//            event.setAttendees(List.of(
//                    new Event.Attendee().setEmail(candidateEmail),
//                    new Event.Attendee().setEmail(recruiterEmail)
//            ));

            service.events().insert("primary", event).execute();
            System.out.println("Calendar invite sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
