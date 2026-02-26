import React from 'react'
import {useState, useEffect} from "react";
import Sidebar from '../../components/layout/sidebar/Sidebar.jsx'

/* function Counter(){
    const [count, setCount] = useState(0);
    
    useEffect(() => {

        if(count===0){
            document.title=`Nema klikova :(`
        }else{
            document.title = `Klikovi: ${count}`;
        }

        
    },[count])

    return(
        <div>
            <p>Kliknuto {count} puta</p>
            <button onClick={()=>setCount(prev =>prev+1)}>Klikni me</button>
            <button onClick={()=>setCount(0)}>Reset</button>
        </div>
    )
}
 */

/* function NameStorage(){
    const [name, setName] = useState("");

    useEffect(()=>{
        const savedName = localStorage.getItem("name");

        if(savedName){
            setName(savedName);
        }
    },[]);

    useEffect(()=>{
        if(name !== ""){
            localStorage.setItem("name", name);
        }
    },[name]);

    return(
        <div>
            <p>Ime: {name}</p>

            <input type="text" value={name} onChange={(e)=> setName(e.target.value)}
            placeholder="Unesi ime" 
            />
        </div>
    )
}
 */

/* function LoginForm(){
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = (e) =>{
        e.preventDefault();
        console.log(email, password);
    };

    return(
        <form onSubmit={handleSubmit}>
            <input type="email"
                    value={email}
                    onChange={(e)=>setEmail(e.target.value)} />

            <input type="password"
                    value={password}
                    onChange={(e)=>setPassword(e.target.value)} />

            <button type="submit">Login</button>
        </form>
    );
} */






export default function ProfileTest(){
    
    return(
        <div>
         {/* <NameStorage /> */}
         {/* <Counter/> */}
         {/* <LoginForm/> */}
            
        </div>
    
    );
}