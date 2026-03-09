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
    const navigate = useNavigate();



    const handleSubmit = async (e)=>{ //Asinhrone f-je omogucavaju data fethcing, api pozive itd...
        e.preventDefault();

        //Backend poziv

        

        const payload = {
            username,
            password,
            email,
            dateOfBirth: selectedDate ? selectedDate.toISOString().split("T")[0] : null
            
          };

          try{
            await registerUser(payload);
            navigate("/login");
          }catch(err){
            console.error(err);
          }
        
          /* console.log(payload); */

  

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

                
                    <p className="ig-date">Date of birth</p>
                    <CalendarIcon className="ig-calendar" value={selectedDate} onChange={setSelectedDate} />
                

                <button type="submit">
                    Register
                </button>

                <p className="ig-">
                    You have an account? <Link to="/login">Login</Link>
                </p>

            </form>
        </div>
    );
}