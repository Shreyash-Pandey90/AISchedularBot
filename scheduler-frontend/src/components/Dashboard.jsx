import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { AppBar, Toolbar, Typography, Button, IconButton, Drawer, List, ListItem, ListItemText, Paper } from '@mui/material';
import Chatbot from './Chatbot';
import '../style/Dashboard.css';

const Dashboard = () => {
  const [chatbotOpen, setChatbotOpen] = useState(false);
  const navigate = useNavigate();

  const candidate = JSON.parse(localStorage.getItem('candidate'));

  const handleLogout = () => {
    localStorage.removeItem('candidate');
    navigate('/');
  };

  return (
    <div>
      {/* AppBar with direct navigation options */}
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" style={{ flexGrow: 1 }}>
            Schedular
          </Typography>
          <Button color="inherit" onClick={() => navigate('/dashboard/calendar')}>
            Calendar
          </Button>
          <Button color="inherit" onClick={() => navigate('/dashboard/meetings')}>
            Upcoming Meetings
          </Button>
          <Button color="inherit" onClick={handleLogout}>
            Logout
          </Button>
        </Toolbar>
      </AppBar>

      {/* Chatbot button */}
      <div style={{ position: 'fixed', bottom: '20px', right: '20px' }}>
        <Button variant="contained" color="primary" onClick={() => setChatbotOpen(!chatbotOpen)}>
          Chatbot
        </Button>
      </div>

      {/* Chatbot modal */}
      {chatbotOpen && (
        <div className="chatbot-modal">
          <Paper elevation={3} className="chatbot-card">
            <Chatbot />
            <Button
              variant="contained"
              color="secondary"
              onClick={() => setChatbotOpen(false)}
              style={{ marginTop: '10px' }}
            >
              Close
            </Button>
          </Paper>
        </div>
      )}
    </div>
  );
};

export default Dashboard;