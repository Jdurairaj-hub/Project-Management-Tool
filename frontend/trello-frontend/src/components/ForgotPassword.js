import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/forgotPassword.css';
import axios from 'axios';

const ForgotPassword = () => {
  const [email, setEmail] = useState('');
  const [securityQuestion, setSecurityQuestion] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleResetPassword = () => {
    setError('');
    if (!email || !securityQuestion || !newPassword || !confirmPassword) {
      setError('Please fill in all fields.');
      return;
    }

    if (newPassword !== confirmPassword) {
      setError('Password and Confirm Password do not match.');
      return;
    }
    const data = {email: email,
      securityAnswer : securityQuestion,
      password: newPassword
      }
  const headers = {'Content-Type': 'application/json'}


  axios.post("http://localhost:8081/user/passwordReset",
      data,{headers:headers})

      .then((response) => {
        if(response.data == "Password reset done"){
         navigate('/login')
        }else{
         setError(response.data);
       }
       })
       .catch((error) => {
        setError('An error occurred. Please try again later.');
      });
  };
  
  return (
    <div className="container">
      <h2>Forgot Password</h2>
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
        <label htmlFor="security-question">[Security Question] Name of Your First Pet?</label>
        <input
          type="text"
          id="security-question"
          placeholder="Enter your security answer"
          value={securityQuestion}
          onChange={(e) => setSecurityQuestion(e.target.value)}
          className="input-field"
        />
      </div>
      <div className="form-group">
        <label htmlFor="new-password">New Password</label>
        <input
          type="password"
          id="new-password"
          placeholder="Enter your new password"
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
          className="input-field"
        />
      </div>
      <div className="form-group">
        <label htmlFor="confirm-password">Confirm Password</label>
        <input
          type="password"
          id="confirm-password"
          placeholder="Confirm your new password"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          className="input-field"
        />
      </div>
      <div className="form-group">
        <button onClick={handleResetPassword} className="reset-button">
          Reset Password
        </button>
      </div>
      <div className="form-group">
        <p>
          Remember your password? <Link to="/login">Login</Link>
        </p>
      </div>
    </div>
  );
};

export default ForgotPassword;
