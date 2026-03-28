import React from "react";
import {Link, useNavigate} from "react-router-dom";
import { useState } from "react";
import "./register.css";
import CalendarIcon from "../../components/common/CalendarIcon";
import {registerUser} from "../../api/authApi";

export default function Login(){
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");
    const [selectedDate, setSelectedDate] = useState(null);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();



    const handleSubmit = async (e)=>{ //Asinhrone f-je omogucavaju data fethcing, api pozive itd...
        e.preventDefault();
        setError("");
        setLoading(true);

        const payload = {
            username,
            password,
            email,
            dateOfBirth: selectedDate ? selectedDate.toISOString().split("T")[0] : null

        };

        try{
            const response = await registerUser(payload);
            console.log("Registration successful:", response);

            // Save auth data if provided in response
            if (response.token) {
                localStorage.setItem("token", response.token);
                localStorage.setItem("userId", response.userId);
                localStorage.setItem("username", response.username);
                if (response.avatar) {
                    localStorage.setItem("avatar", response.avatar);
                }
                // Auto-login after registration
                navigate("/feed");
            } else {
                // If no token, go to login
                navigate("/login");
            }
        }catch(err){
            console.error("Registration error:", err);
            setError(err.message || "Registration failed. Please try again.");
        } finally {
            setLoading(false);
        }
    };

    return(
        <div className="ig-login">
            <form className="ig-login_form" onSubmit={handleSubmit}>
                <h2>Register</h2>

                {error && <div style={{ color: "red", marginBottom: "10px", fontSize: "14px" }}>{error}</div>}

                <input type="email" placeholder="E-mail" value={email} onChange={(e)=>setEmail(e.target.value)} required/>

                <input type="text" placeholder="Username" value={username} onChange={(e)=>setUsername(e.target.value)}  required/>

                <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} required />


                <p className="ig-date">Date of birth</p>
                <CalendarIcon className="ig-calendar" value={selectedDate} onChange={setSelectedDate} />


                <button type="submit" disabled={loading}>
                    {loading ? "Registering..." : "Register"}
                </button>

                <p className="ig-">
                    You have an account? <Link to="/login">Login</Link>
                </p>

            </form>
        </div>
    );
}