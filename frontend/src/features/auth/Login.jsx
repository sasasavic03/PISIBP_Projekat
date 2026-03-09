import React from "react";
import {Link, useNavigate} from "react-router-dom";
import { useState } from "react";
import "./login.css";

export default function Login(){
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();


    const handleSubmit = async (e)=>{
        e.preventDefault();

        const payload = {
            username,
            password
        };

        try{
            const response = await fetch("http://localhost:8080/api/auth/login",{
                method: "POST",
                headers:{
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload)
            });

            if(!response.ok){
                throw new Error("Login failed");
            }

            const data = await response.json();

            console.log("Login succes", data);

            if(data.token){
                localStorage.setItem("token", data.token);
            }
            navigate("/feed");
        } catch(error){
            console.error("Login error:" , error);
        }

        //Backend poziv

        //Ispravan login
        /* navigate("/home"); */
    };

    return(
        <div className="ig-login">
            <form className="ig-login_form" onSubmit={handleSubmit}>
                <h2>Login</h2>

                <input type="text" placeholder="Username" value={username} onChange={(e)=>setUsername(e.target.value)}  required/>

                <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} required />

                <button type="submit">
                    Log in
                </button>

                <p>
                    You don't have an account? <Link to="/register">Regsiter now</Link>
                </p>

            </form>
        </div>
    );
}