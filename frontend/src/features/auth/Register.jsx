import React from "react";
import {Link, useNavigate} from "react-router-dom";
import { useState } from "react";
import "./login.css";
import CalendarIcon from "../../components/common/CalendarIcon";

export default function Login(){
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");
    const [selectedDate, setSelectedDate] = useState(null);
    const navigate = useNavigate();



    const handleSubmit = (e)=>{
        e.preventDefault();

        //Backend poziv

        

        const payload = {
            username,
            password,
            email,
            dateOfBirth: selectedDate, 
            dateOfBirthISO: selectedDate ? selectedDate.toISOString() : null,
          };
        
          console.log(payload);

  

        //Ispravan login
        /* navigate("/home"); */
    };

    return(
        <div className="ig-login">
            <form className="ig-login_form" onSubmit={handleSubmit}>
                <h2>Register</h2>

                <input type="email" placeholder="E-mail" value={email} onChange={(e)=>setEmail(e.target.value)} required/>

                <input type="text" placeholder="Username" value={username} onChange={(e)=>setUsername(e.target.value)}  required/>

                <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} required />

                
                    <h5>Date of birth</h5>
                    <CalendarIcon value={selectedDate} onChange={setSelectedDate} />
                

                <button type="submit">
                    Register
                </button>

                <p>
                    You don't have an account? <Link to="/register">Regsiter now</Link>
                </p>

            </form>
        </div>
    );
}