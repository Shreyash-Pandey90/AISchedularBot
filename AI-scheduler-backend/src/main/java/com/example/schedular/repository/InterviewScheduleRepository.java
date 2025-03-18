package com.example.schedular.repository;

import com.example.schedular.model.InterviewSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InterviewScheduleRepository extends MongoRepository<InterviewSchedule, String> {
    List<InterviewSchedule> findByCandidateEmail(String candidateEmail);
    List<InterviewSchedule> findByRecruiterEmail(String recruiterEmail);

}
