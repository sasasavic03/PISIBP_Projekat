import React from "react";
import "./followersmodal.css";
import { Link } from "react-router-dom";

export default function FollowersModal({ type, users, loading ,onClose }) {
  return (
    <div className="ig-followersmodal-backdrop" onClick={onClose}>
      <div className="ig-followersmodal" onClick={(e) => e.stopPropagation()}>

        <div className="ig-followersmodal-header">
          <span>{type === "followers" ? "Followers" : "Following"}</span>
          <button className="ig-followersmodal-close" onClick={onClose}>✕</button>
        </div>

        <div className="ig-followersmodal-list">
                    {loading ? (
            <div style={{ textAlign: 'center', padding: '20px' }}>Loading...</div>
          ) : users && users.length > 0 ? (
            users.map((user) => (
              <div key={user.id} className="ig-followersmodal-item">
                <Link
                  to={`/profile/${user.username}`}
                  onClick={onClose}
                  className="ig-followersmodal-link"
                >
                  <img src={user.avatar || "/default-avatar.svg"} alt={user.username} />
                  <span className="ig-followersmodal-username">{user.username}</span>
                </Link>
                <button className="ig-followersmodal-follow-btn">Follow</button>
              </div>
            ))
          ) : (
            <div style={{ textAlign: 'center', padding: '20px' }}>No users found</div>
          )}
        </div>

      </div>
    </div>
  );
}