import React, { useState, useEffect } from "react";
import {useParams} from "react-router-dom";
import ProfileHeader from "./ProfileHeader";
import PostsGrid from "./PostsGrid";
import "./profile.css"
import {getProfileByUsername} from "../../data/profile.mock";

//mock
const blockedUsersMock = ["jane.smith"];

//simulacija ulogovanog korisnika
const loggedUser = {id:1, username:"john.doe"};

export default function ProfilePage(){

  const [profile,setProfile] = useState(null);
  const [blocked, setBlocked] = useState(blockedUsersMock);
  const {username} = useParams();

  //simulacija ulogovanog korisnika
  const loggedUserId = 1;

  useEffect(()=>{

    const found = getProfileByUsername(username);
    setProfile(found);

  },[username]);


  if(!profile){
    return <div>Loading...</div>;
  }

  const isOwnProfile = profile.user.id === loggedUserId;
  const isBlocked = blocked.includes(username);
  const isFollowing = profile.user.followers?.some(f => f.username === loggedUser.username);
  const isPrivate = profile.user.isPrivate;
  const canViewContent = isOwnProfile || isFollowing || !isPrivate;

  function unblock() {
    setBlocked(blocked.filter(u => u !== username));
  }

  if (isBlocked) {
    return (
      <div className="profile">
        <div className="ig-blocked-view">
          <img
            src={profile.user.avatar}
            alt={profile.user.username}
            className="ig-blocked-avatar"
          />
          <h2 className="ig-blocked-username">{profile.user.username}</h2>
          <p className="ig-blocked-text">You have blocked this account.</p>
          <button className="ig-blocked-unblock-btn" onClick={unblock}>
            Unblock
          </button>
        </div>
      </div>
    );
  }

  /* blockedUserMock niz i unblock fju zameni sa api pozivima */
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
            
            {canViewContent ? (
              <section className="profile-grid">
                  <PostsGrid posts={profile.posts} isOwnProfile={isOwnProfile} />
              </section>
            ) : (
              <div className="ig-private-view">
                <div className="ig-private-icon">🔒</div>
                <h3 className="ig-private-title">This account is private</h3>
                <p className="ig-private-text">Follow this account to see their photos and videos.</p>
              </div>
            )}
          
        </div>
    );
}