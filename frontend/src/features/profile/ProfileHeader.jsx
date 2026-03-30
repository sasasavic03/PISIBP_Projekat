import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import "./profileheader.css";
import FollowersModal from "./followersmodal/FollowersModal";
import ProfileOptionsModal from "./profile_options_modal/ProfileOptionsModal";
import { followUser, unfollowUser, checkFollow, cancelFollowRequest } from "../../api/followApi";

export default function ProfileHeader({ user, stats, isOwnProfile }) {

  const [modalType, setModalType] = useState(null);
  const [followed, setFollowed] = useState(false);
  const [requested, setRequested] = useState(false);
  const [showOptions, setShowOptions] = useState(false);

  const currentUserId = parseInt(localStorage.getItem("userId")) || null;

  
  useEffect(() => {
    if (!isOwnProfile && user?.id) {
      const checkFollowStatus = async () => {
        try {
          const data = await checkFollow(user.id);
          setFollowed(data.following || false);
          setRequested(data.requested || false);
        } catch (err) {
          console.error("Failed to check follow status:", err);
        }
      };
      checkFollowStatus();
    }
  }, [user, isOwnProfile]);


  async function handleFollow() {
    try {
      if (followed) {
        await unfollowUser(user.id);
        setFollowed(false);
        setRequested(false);
      } else if (user?.isPrivate && !requested) {
        const data = await followUser(user.id, user.isPrivate);
        if (data.status === "requested") {
          setRequested(true);
        } else {
          setFollowed(true);
        }
      } else if (requested) {
        await unfollowUser(user.id);
        await cancelFollowRequest(user.id);
        setRequested(false);
      } else {
        await followUser(user.id);
        setFollowed(true);
      }
    } catch (err) {
      console.error("Failed to follow/unfollow:", err);
    }
  }

  return (
    <div className="ig-profile-header">

      <div className="ig-profile-avatar">
        <img src={user?.profilePictureUrl || "/default-avatar.svg"} alt="profile" />
      </div>

      <div className="ig-profile-info">

        <div className="ig-profile-top">
          <h2 className="ig-username">{user?.username}</h2>

          {isOwnProfile ? (
            <div>
              <Link to="/settings" className="ig-settings-btn">Settings</Link>
            </div>
          ) : (
            <div className="ig-profile-actions">
              <button
                className={
                  followed ? "ig-unfollow-btn" :
                  requested ? "ig-requested-btn" :
                  "ig-follow-btn"
                }
                onClick={handleFollow}
              >
                {followed ? "Unfollow" : requested ? "Requested" : "Follow"}
              </button>

              <button className="ig-message-btn">Message</button>

              <button className="ig-options-btn" onClick={() => setShowOptions(true)}>
                ···
              </button>
            </div>
          )}

          {showOptions && (
            <ProfileOptionsModal
              userId={user?.id}
              onClose={() => setShowOptions(false)}
            />
          )}

        </div>

        <div className="ig-profile-bio">
          <strong>{user?.fullName}</strong>
          <p>{user?.bio}</p>
        </div>

        <div className="ig-profile-stats">
          <span>
            <strong>{stats?.posts}</strong> posts
          </span>
          <span
            style={{ cursor: "pointer" }}
            onClick={() => setModalType("followers")}
          >
            <strong>{stats?.followers}</strong> followers
          </span>
          <span
            style={{ cursor: "pointer" }}
            onClick={() => setModalType("following")}
          >
            <strong>{stats?.following}</strong> following
          </span>
        </div>

        {modalType && (
          <FollowersModal
            type={modalType}
            users={modalType === "followers" ? user.followers : user.following}
            onClose={() => setModalType(null)}
          />
        )}

        {!isOwnProfile && user?.followedBy?.length > 0 && (
          <div className="ig-followed-by">
            <div className="ig-followed-avatars">
              {user.followedBy.slice(0, 3).map((follower) => (
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