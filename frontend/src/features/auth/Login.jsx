import React from "react";
import {Link, useNavigate} from "react-router-dom";
import { useState } from "react";
import "./login.css";
import { loginUser } from "../../api/authApi";

export default function Login(){
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(null);
    const navigate = useNavigate();


    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
    
        try {
          const data = await loginUser({ username, password });
    
          if (data.token) {
            localStorage.setItem("token", data.token);
            localStorage.setItem("userId", data.userId);
            localStorage.setItem("username", data.username);
            localStorage.setItem("avatar", data.avatar);
          }
    
          navigate("/feed");
        } catch (err) {
          setError("Invalid username or password.");
          console.error("Login error:", err);
        }
      };

      return (
        <div className="ig-login">
          <form className="ig-login_form" onSubmit={handleSubmit}>
            <h2>Login</h2>
    
            <input
              type="text"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
    
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
    
            {error && <p className="ig-login-error">{error}</p>}
    
            <button type="submit">Log in</button>
    
            <p>
              You don't have an account? <Link to="/register">Register now</Link>
            </p>
          </form>
        </div>
      );
    }