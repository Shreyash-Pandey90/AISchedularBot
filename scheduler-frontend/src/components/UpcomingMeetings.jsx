import React, { useEffect, useState } from 'react';
import { Typography, List, ListItem, ListItemText, Paper } from '@mui/material';
import CandidateService from '../services/candidate';
import '../style/UpcomingMeetings.css';


const UpcomingMeetings = () => {
  const [interviews, setInterviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Fetch the logged-in candidate's email from localStorage
  const candidate = JSON.parse(localStorage.getItem('candidate'));
  const candidateEmail = candidate ? candidate.email : '';

  useEffect(() => {
    const fetchInterviews = async () => {
      try {
        // Fetch interviews for the logged-in candidate
        const response = await CandidateService.getSchedulesForCandidate(candidateEmail);
        setInterviews(response);
      } catch (error) {
        setError(error.message || 'Failed to fetch interviews.');
      } finally {
        setLoading(false);
      }
    };

    if (candidateEmail) {
      fetchInterviews();
    }
  }, [candidateEmail]);

  if (loading) {
    return <Typography>Loading...</Typography>;
  }

  if (error) {
    return <Typography color="error">{error}</Typography>;
  }

  return (
    <Paper className="upcoming-meetings-container">
      <Typography variant="h5" gutterBottom>
        Upcoming Meetings
      </Typography>
      {interviews.length > 0 ? (
        <List className="meeting-list">
          {interviews.map((interview, index) => (
            <ListItem key={index} className="meeting-item">
              <ListItemText
                primary={`Interview with ${interview.recruiterEmail}`}
                secondary={`Scheduled at: ${new Date(interview.interviewTime).toLocaleString()}`}
              />
            </ListItem>
          ))}
        </List>
      ) : (
        <Typography>No upcoming meetings.</Typography>
      )}
    </Paper>
  );
};

export default UpcomingMeetings;