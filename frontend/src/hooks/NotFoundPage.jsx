import React from "react"
import { Link } from "react-router"

export default function NotFoundPage(){

    return(
        <>
            <p>Page Not Found</p><br />
            <p>Retur to home page</p><br />
            <Link to="/">Home</Link>
        </>
        
    );
}