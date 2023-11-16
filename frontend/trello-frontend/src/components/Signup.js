import React, { useState } from 'react';
import {Link, useNavigate} from 'react-router-dom';
import '../styles/signup.css';
import axios from 'axios';


const Signup = () => {

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [securityQuestion, setSecurityQuestion] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const navigate = useNavigate();

  const handleSignup = () => {
    // Perform basic password validation
    const passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]).{8,}$/;
    if (!passwordRegex.test(password)) {
      setError(
        'Password must have at least 8 characters, including 1 uppercase, 1 lowercase, 1 number, and 1 special character.'
      );
      return;
    }

    if (password !== confirmPassword) {
      setError('Password and Confirm Password do not match.');
      return;
    }

    const securityRegex = /^[A-Za-z]+$/;

    if (!securityRegex.test(securityQuestion)) {
      setError(
            'Please enter security answer.'
        );
        return;
    }

    setLoading(true);

    const data = {
        email: email,
        password: password,
        securityAnswer: securityQuestion
    }

    const headers = {'Content-Type': 'application/json'}


    axios.post("http://localhost:8081/user/signUp",
        data, {headers: headers})
        .then((response) => {
            if(response.data == "User registered successfully"){
                alert("You have successfully signed up !")
                navigate('/login')
            }else{
                setError(response.data);
            }
        })
        .catch((error) => {
          setError('An error occurred. Please try again later.');
        })
        .finally(() => {
          setLoading(false);
        });
    };

  return (
    <div className="container">
      <h2>Sign Up</h2>
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
        <label htmlFor="confirm-password">Confirm Password</label>
        <input
          type="password"
          id="confirm-password"
          placeholder="Confirm your password"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
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
        <button onClick={handleSignup} className="signup-button">
          Sign Up
        </button>
      </div>
      <div className="form-group">
        <p>
          Already have an account? <Link to="/login">Login</Link>
        </p>
      </div>
    </div>
  );
};

export default Signup;
