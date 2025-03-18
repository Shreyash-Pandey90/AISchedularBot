import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Container, Typography } from '@mui/material';
import '../style/Home.css'; // Import the CSS file

const Home = () => {
  const navigate = useNavigate();

  return (
    <Container className="home-container">
      <Typography variant="h3" align="center" gutterBottom className="welcome-text">
        Welcome to Schedular
      </Typography>
      <div className="horizontal-line"></div>
      <div className="button-container">
        <Button variant="contained" color="primary" onClick={() => navigate('/candidate')} className="candidate-button">
          Candidate
        </Button>
        <Button variant="contained" color="secondary" onClick={() => navigate('/recruiter/login')} className="recruiter-button">
          Recruiter
        </Button>
      </div>
    </Container>
  );
};

export default Home;