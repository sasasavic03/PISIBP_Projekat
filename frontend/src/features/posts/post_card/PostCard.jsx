import React from "react";
import { Link } from "react-router-dom";
import "./post_card.css";
import { FiHeart,FiMessageCircle,FiSend } from "react-icons/fi";

export default function PostCard({
  author,
  avatar,
  image,
  likes,
  content
}) {
  
  return (
    <article className="ig-post_card">
      
      {/* header */}
      <header className="ig-post_header">
        <Link to={`/profile/${author}`} className="ig-post_user">
          <img
            src={avatar}
            alt={`${author} avatar`}
            className="ig-post_avatar"
          />
          <span className="ig-post_username">{author}</span>
        </Link>
      </header>

      {/* imgage */}
      <div className="ig-post_media">
        <img
          src={image}
          alt="post"
        />
      </div>

      {/* actions */}
      <div className="ig-post_actions">
        <button><FiHeart /></button>
        <button><FiMessageCircle/></button>
        <button><FiSend/></button>
      </div>

      {/* content */}
      <div className="ig-post_content">
        <p className="ig-post_likes">{likes} likes</p>
        <p>
          <span className="ig-post_username">{author}</span>{" "}
          {content}
        </p>
      </div>

    </article>
  );
}
