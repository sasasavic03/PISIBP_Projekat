import React from "react";
import "./likedby_modal.css";

export default function LikedByModal({ likedBy, onClose }) {
  return (
    <div className="ig-likedby-backdrop" onClick={onClose}>
      <div className="ig-likedby-modal" onClick={(e) => e.stopPropagation()}>

        <div className="ig-likedby-header">
          <span>Likes</span>
          <button className="ig-likedby-close" onClick={onClose}>✕</button>
        </div>

        <div className="ig-likedby-list">
          {likedBy.map((user) => (
            <div key={user.id} className="ig-likedby-item">
              <img src={user.avatar} alt={user.username} />
              <span className="ig-likedby-username">{user.username}</span>
              <button className="ig-likedby-follow-btn">Follow</button>
            </div>
          ))}
        </div>

      </div>
    </div>
  );
}