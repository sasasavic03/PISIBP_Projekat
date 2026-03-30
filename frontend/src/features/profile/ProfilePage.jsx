import React, { useState, useEffect } from "react";
import {useParams} from "react-router-dom";
import ProfileHeader from "./ProfileHeader";
import PostsGrid from "./PostsGrid";
import "./profile.css"
import { getUserProfile, getUserStats, getBlockedUsers, unblockUser } from "../../api/userApi";
import { checkFollow } from "../../api/followApi";

const loggedUser = {
  id: parseInt(localStorage.getItem("userId")) || 1,
  username: localStorage.getItem("username") || "john.doe",
};

export default function ProfilePage(){

  const [profile,setProfile] = useState(null);
  const [blocked, setBlocked] = useState([]);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null)
  const [isFollowing, setIsFollowing] = useState(false);

  const {username} = useParams();

  



  useEffect(() => {
    async function fetchData() {
      setLoading(true);
      setError(null);

      try {
        const [profileData, statsData, blockedData] = await Promise.all([
          getUserProfile(username),
          getUserStats(username),
          getBlockedUsers(loggedUser.id),
        ]);

        setProfile(profileData);
        setStats(statsData);
        setBlocked(blockedData.map(u => u.username));

        if (profileData.id !== loggedUser.id) {
          try {
            const followData = await checkFollow(profileData.id);
            setIsFollowing(followData.following ?? false);
          } catch (err) {
            console.error("Failed to check follow status:", err);
          }
        }

      } catch (err) {
        setError(err.message);
        console.error("Failed to fetch profile:", err);
      } finally {
        setLoading(false);
      }
    }

    fetchData();
  }, [username]);


  async function handleUnblock() {
    try {
      await unblockUser(profile.id);
      setBlocked(blocked.filter(u => u !== username));
    } catch (err) {
      console.error("Failed to unblock:", err);
    }
  }

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;
  if (!profile) return <div>Profile not found.</div>;

  const isOwnProfile = profile.id === loggedUser.id;
  const isBlocked = blocked.includes(username);
  const isPrivate = profile.isPrivate;
  const canViewContent = isOwnProfile || isFollowing || !isPrivate;

  if (isBlocked) {
    return (
      <div className="profile">
        <div className="ig-blocked-view">
        <img src={profile?.profilePictureUrl || "/default-avatar.svg"} alt={profile.username} className="ig-blocked-avatar" />
          <h2 className="ig-blocked-username">{profile.username}</h2>
          <p className="ig-blocked-text">You have blocked this account.</p>
          <button className="ig-blocked-unblock-btn" onClick={handleUnblock}>
            Unblock
          </button>
        </div>
      </div>
    );
  }



  return (
    <div className="profile">
      <section className="profile-header">
        <ProfileHeader
          user={profile}
          stats={{
            posts: stats?.postsCount || 0,
            followers: stats?.followersCount || 0,
            following: stats?.followingCount || 0
          }}
          isOwnProfile={isOwnProfile}
        />
      </section>

      {canViewContent ? (
        <section className="profile-grid">
          <PostsGrid username={username} isOwnProfile={isOwnProfile} />
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