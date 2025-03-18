import React, { useState } from 'react';
import { useNavigate,useLocation } from 'react-router-dom';
import { TextField, Button, Container, Typography } from '@mui/material';
import CandidateService from '../services/candidate';
import '../style/login.css'; // Import the CSS file

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
  const location = useLocation();


  const handleLogin = async () => {
    try {
      const response = await CandidateService.login(email, password);
      if (response) {
        localStorage.setItem('candidate', JSON.stringify(response));
        navigate('/dashboard');
      }
    } catch (error) {
      console.error('Login failed:', error);
      alert(error.message || 'An error occurred during login.');
    }
  };

  const handleGoogleLogin = () => {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
  };

  return (
    <div className="login-container">
      {/* Left side for the image */}
      <div className="login-image"></div>

      {/* Right side for the login card */}
      <div className="login-card">
        <div className="card">
          <Typography variant="h4" gutterBottom>
            Candidate Login
          </Typography>
          <div className="login-form">
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
              onClick={handleLogin}
              className="login-button"
            >
              Login
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

            {location.pathname === '/candidate' && (
            <Button
              variant="text"
              onClick={() => navigate('/register')}
              className="register-button"
            >
              Register
            </Button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;