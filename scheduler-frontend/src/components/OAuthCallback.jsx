import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const OAuthCallback = () => {
  const navigate = useNavigate();

  useEffect(() => {
    // Fetch the authenticated user's details from the backend
    const fetchUser = async () => {
      try {
        // Call the backend endpoint to get the logged-in user details
        const response = await fetch('http://localhost:8080/api/candidates/login-success', {
          credentials: 'include', // Include cookies for session management
        });

        if (!response.ok) {
          throw new Error('Failed to fetch user details');
        }

        const user = await response.json();

        if (user) {
          // Save the user details in localStorage
          localStorage.setItem('candidate', JSON.stringify(user));
          // Redirect to the dashboard
          navigate('/dashboard');
        } else {
          // If no user is returned, redirect to the login page
          navigate('/login');
        }
      } catch (error) {
        console.error('Error during OAuth2 callback:', error);
        // Redirect to the login page in case of an error
        navigate('/login');
      }
    };

    fetchUser();
  }, [navigate]);

  return (
    <div style={{ textAlign: 'center', marginTop: '50px' }}>
      <Typography variant="h5">Loading...</Typography>
      <CircularProgress />
    </div>
  );
};

export default OAuthCallback;