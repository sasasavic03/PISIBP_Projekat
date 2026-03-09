import React, { useState, useEffect } from "react";
import ProfileHeader from "./ProfileHeader";
import PostsGrid from "./PostsGrid";
import "./profile.css"
import {profileMock} from "../../data/profile.mock";



export default function ProfilePage(){

  const [profile,setProfile] = useState(null);

  //simulacija ulogovanog korisnika
  const loggedUserId = 1;

  useEffect(()=>{

    setProfile(profileMock);

  },[]);


  if(!profile){
    return <div>Loading...</div>;
  }

  const isOwnProfile = profile.user.id === loggedUserId;

  //kad stigne back zameni useEffect
  /* 
  useEffect(()=>{

  async function fetchProfile(){

    const response = await fetch("http://localhost:8080/api/users/profile");
    const data = await response.json();

    setProfile(data);
  }

  fetchProfile();

},[]);
   */

    return(
        <div className="profile">
            <section className="profile-header">
            <ProfileHeader 
            user={profile.user}
            stats={profile.stats}
            isOwnProfile={isOwnProfile}/>
            
            </section>
            <section className="profile-followers">

            </section>
            <section className="profile-grid">
                <PostsGrid posts={profile.posts} />
            </section>
        </div>
    );
}