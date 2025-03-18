import React, { useState } from 'react';
import { TextField, Button, Paper, Typography } from '@mui/material';
import '../style/Chatbot.css'; // Import the CSS file

const Chatbot = () => {
  const [message, setMessage] = useState('');
  const [response, setResponse] = useState('');

  const handleSend = () => {
    // Simulate chatbot response
    setResponse(`You said: ${message}`);
    setMessage('');
  };

  return (
    <Paper className="chatbot-container">
      <Typography variant="h6" className="chatbot-title">
        Chatbot
      </Typography>
      <div className="chatbot-messages">
        {response && <Typography className="response-text">{response}</Typography>}
      </div>
      <div className="chatbot-input-container">
        <TextField
          fullWidth
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          placeholder="Type your message..."
          className="chatbot-input"
        />
        <Button
          variant="contained"
          color="primary"
          onClick={handleSend}
          className="send-button"
        >
          Send
        </Button>
      </div>
    </Paper>
  );
};

export default Chatbot;