import React, { useState } from "react";
import { Link } from "react-router-dom";
import "./post_card.css";
import { FiHeart, FiMessageCircle, FiSend, FiChevronLeft, FiChevronRight } from "react-icons/fi";
import PostModal from "../post_modal/PostModal";

export default function PostCard({
  author,
  avatar,
  images,
  likes:initialLikes,
  content,
  comments = []
}) {

  const [currentIndex, setCurrentIndex] = useState(0);
  const [liked, setLiked] = useState(false);
  const [likes, setLikes] = useState(initialLikes);
  const [showModal, setShowModal] = useState(false);
  const [showHeart, setShowHeart] = useState(false);

  function nextImage(){
    setCurrentIndex((prev)=>
      prev === images.length - 1 ? prev : prev + 1
    );
  }

  function prevImage(){
    setCurrentIndex((prev)=>
      prev === 0 ? prev : prev - 1
    );
  }

  function toggleLike() {
    if (liked) {
      setLikes(likes - 1);
      setLiked(false);
    } else {
      setLikes(likes + 1);
      setLiked(true);
    }
  }

  function doubleClickLike() {
    if (!liked) {
      setLikes(likes + 1);
      setLiked(true);
    }
    setShowHeart(true);
    setTimeout(() => setShowHeart(false), 700);
  }

  const postForModal = {
    images,
    username: author,
    likes,
    comments,
  };

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

      {/* image carousel */}
      <div className="ig-post_media" onDoubleClick={doubleClickLike}>

        <img
          src={images[currentIndex]}
          alt="post"
        />

        {showHeart && (
            <div className="ig-heart-animation">❤️</div>
         )}

        {images.length > 1 && (
          <>
            {currentIndex > 0 && (
              <button
                className="ig-carousel_btn ig-prev"
                onClick={prevImage}
              >
                <FiChevronLeft  />
              </button>
            )}

            {currentIndex < images.length - 1 && (
              <button
                className="ig-carousel_btn ig-next"
                onClick={nextImage}
              >
                <FiChevronRight  />
              </button>
            )}
          </>
        )}

      </div>

      {/* carousel tacke */}
      {images.length > 1 && (
        <div className="ig-carousel_dots">
          {images.map((_, i) => (
            <span
              key={i}
              className={`ig-carousel_dot${i === currentIndex ? " active" : ""}`}
            />
          ))}
        </div>
      )}

      {/* actions */}
      <div className="ig-post_actions">
        <button onClick={toggleLike}>
          {liked
            ? <FiHeart stroke="black" fill="red" />
            : <FiHeart />
          }
        </button>
        <button onClick={() => setShowModal(true)}>
          <FiMessageCircle />
        </button>
        <button><FiSend /></button>
      </div>

      {/* content */}
      <div className="ig-post_content">
        <p className="ig-post_likes">{likes} likes</p>
        <p>
          <span className="ig-post_username">{author}</span>{" "}
          {content}
        </p>
      </div>


      {/* modal */}
      {showModal && (
        <PostModal
          post={postForModal}
          onClose={() => setShowModal(false)}
        />
      )}

    </article>
  );
}