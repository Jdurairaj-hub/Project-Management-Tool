import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/login.css';
import axios from 'axios';

const Login = ({handleLogin2}) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = () => {
    
    // Perform login logic here
    // Verify email and password from the database or API
    // Set flag in local storage to indicate user is logged in
    setError('');

    if (!email || !password) {
      setError('Please enter your email and password.');
      return;
    }
    
    const data = {
      email: email,
      password: password
    };
    const headers = {
      'Content-Type': 'application/json'
    };

    axios
      .post("http://localhost:8081/user/logIn", data, { headers: headers })
      .then((response) => {
        if (response.data === "User logged in successfully") {
          localStorage.setItem('isLoggedIn', 'true');
          localStorage.setItem('email', email);
          //localStorage.setItem('workspaceData',);
          //localStorage.setItem('boardspaceData',);
          alert('Logged in successfully!');
       
          navigate('/workspace');
          
        } else {
          setError('Invalid email or password.');
          alert('Invalid email or password.');
        }
      })
      .catch((error) => {
        setError('An error occurred. Please try again later.');
      });
    };

  return (
    <div className="container">
      <h2>Login</h2>
      {error && <p className="error">{error}</p>}
      <div className="form-group">
        <label htmlFor="email">Email</label>
        <input
          type="email"
          id="email"
          placeholder="Enter your email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="input-field"
        />
      </div>
      <div className="form-group">
        <label htmlFor="password">Password</label>
        <input
          type="password"
          id="password"
          placeholder="Enter your password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="input-field"
        />
      </div>
      <div className="form-group">
        <button type="submit" onClick={handleLogin} className="login-button">
          Login
        </button>
      </div>
      <div className="form-group">
        <p>
          Don't have an account? <Link to="/signup">Sign Up</Link>
        </p>
      </div>
      <div className="form-group">
        <p>
          <Link to="/forgot-password">Forgot Password?</Link>
        </p>
      </div>
    </div>
  );
};

export default Login;
