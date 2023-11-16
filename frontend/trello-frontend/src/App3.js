import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import SignupPage from './pages/SignupPage';
import LoginPage from './pages/LoginPage';
import ForgotPasswordPage from './pages/ForgotPasswordPage';
import Workspace from './pages/workspace';
//import ProtectedRoute from './ProtectedRoute';
import Login from './components/Login';

const ProtectedRoute = ({ component: Component, isLoggedIn, ...rest }) => {
  return (
    <Route
      {...rest}
      element={!isLoggedIn ? <Navigate to="/login" replace /> : <Component />}
    />
  );
};

function App() {
  
  const  [isLoggedIn,setIsLoggedIn] = useState(false);
  
  const handleLogin2 = () => {
    setIsLoggedIn(true);
  };

  return (
    <Router>
      <div>
        <Routes>
          <Route path="/" element={<Login handleLogin2={handleLogin2} />} />
          <Route path="/signup" element={<SignupPage />} />
          <Route path="/login" element={<Login handleLogin2={handleLogin2} />} />
          <Route path="/forgot-password" element={<ForgotPasswordPage />} />
          {/* Use ProtectedRoute to protect the Workspace component */}
          <Route path="/workspace" element={<ProtectedRoute component={Workspace} isLoggedIn={isLoggedIn} />} />
        </Routes>
      </div>
    </Router>
  );
}
export default App;
