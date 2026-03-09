import React from "react";
import "./profileheader.css";

export default function ProfileHeader({ user, stats, isOwnProfile }) {

  return (
    <div className="ig-profile-header">

      <div className="ig-profile-avatar">
        <img src={user?.avatar} alt="profile" />
      </div>

      <div className="ig-profile-info">

        <div className="ig-profile-top">
          <h2 className="ig-username">{user?.username}</h2>

          {isOwnProfile ? (
            <button className="ig-edit-btn">
              Edit profile
            </button>
          ) : (
            <div className="ig-profile-actions">

              <button className="ig-follow-btn">
                Follow
              </button>

              <button className="ig-message-btn">
                Message
              </button>

            </div>
          )}

        </div>

        <div className="ig-profile-bio">
          <strong>{user?.fullName}</strong>
          <p>{user?.bio}</p>
        </div>

        <div className="ig-profile-stats">
          <span>
            <strong>{stats.posts}</strong> posts
          </span>

          <span>
            <strong>{stats.followers}</strong> followers
          </span>

          <span>
            <strong>{stats.following}</strong> following
          </span>
        </div>

        

        {/* Followed by sekcija */}
        {!isOwnProfile && user?.followedBy?.length > 0 && (
          <div className="ig-followed-by">

            <div className="ig-followed-avatars">
              {user.followedBy.slice(0,3).map((follower)=>(
                <img
                  key={follower.id}
                  src={follower.avatar}
                  alt={follower.username}
                />
              ))}
            </div>

            <span>
              Followed by{" "}
              <strong>{user.followedBy[0].username}</strong>
              {user.followedBy.length > 1 && (
                <> and <strong>{user.followedBy.length - 1} others</strong></>
              )}
            </span>

          </div>
        )}

      </div>

    </div>
  );
}