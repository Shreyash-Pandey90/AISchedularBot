import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './components/Home';
import Login from './components/Login';
import Register from './components/Register';
import Dashboard from './components/Dashboard';
import UpcomingMeetings from './components/UpcomingMeetings';
import OAuthCallback from './components/OAuthCallback';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/candidate" element={<Login />} />       
        <Route path="/register" element={<Register />} />
        <Route path="/oauth-callback" element={<OAuthCallback />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/dashboard/meetings" element={<UpcomingMeetings />}/>
        <Route path="/recruiter/login" element={<Login/>}></Route>
      </Routes>
    </Router>
  );
}

export default App;