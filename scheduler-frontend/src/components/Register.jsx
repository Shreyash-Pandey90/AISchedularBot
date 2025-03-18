import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { TextField, Button, Container, Typography } from '@mui/material';
import CandidateService from '../services/candidate';
import '../style/Register.css'; 
const Register = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleRegister = async () => {
    try {
      const candidateData = { name, email, password, availableSlots: [] };
      const response = await CandidateService.register(candidateData);
      if (response) {
        navigate('/login');
      }
    } catch (error) {
      console.error('Registration failed:', error);
      alert(error.message || 'An error occurred during registration.');
    }
  };

  const handleGoogleLogin = () => {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
  };

  return (
    <div className="register-container">
      {/* Left side for the image */}
      <div className="register-image"></div>

      {/* Right side for the register card */}
      <div className="register-card">
        <div className="card">
          <Typography variant="h4" gutterBottom>
            Candidate Registration
          </Typography>
          <div className="register-form">
            <TextField
              label="Name"
              fullWidth
              margin="normal"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
            <TextField
              label="Email"
              fullWidth
              margin="normal"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <TextField
              label="Password"
              type="password"
              fullWidth
              margin="normal"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <Button
              variant="contained"
              color="primary"
              onClick={handleRegister}
              className="register-button"
            >
              Register
            </Button>

             {/* Continue with Google Button */}
                        <Button
                          variant="contained"
                          color="secondary"
                          onClick={handleGoogleLogin}
                          className="google-button"
                          style={{ marginTop: '10px', backgroundColor: '#DB4437', color: '#fff' }}
                        >
                          Continue with Google
                        </Button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Register;