import React, { useState } from "react";
import { Link } from "react-router-dom";
import "./post_card.css";
import { FiHeart, FiMessageCircle, FiSend, FiChevronLeft, FiChevronRight } from "react-icons/fi";
import PostModal from "../post_modal/PostModal";
import { likePost, unlikePost } from "../../../api/likeApi";

export default function PostCard({
  id,
  author,
  avatar,
  images,
  mediaList = [],
  likes: initialLikes,
  content,
  comments = [],
  likedBy: initialLikedBy = []
}) {

  const [currentIndex, setCurrentIndex] = useState(0);
  const [liked, setLiked] = useState(false);
  const [likes, setLikes] = useState(initialLikes);
  const [showModal, setShowModal] = useState(false);
  const [showHeart, setShowHeart] = useState(false);

  function nextImage() {
    setCurrentIndex((prev) => prev === images.length - 1 ? prev : prev + 1);
  }

  function prevImage() {
    setCurrentIndex((prev) => prev === 0 ? prev : prev - 1);
  }

  async function toggleLike() {
    try {
      if (liked) {
        const data = await unlikePost(id);
        setLikes(data.likesCount);
        setLiked(false);
      } else {
        const data = await likePost(id);
        setLikes(data.likesCount);
        setLiked(true);
      }
    } catch (err) {
      console.error("Failed to toggle like:", err);
    }
  }

  async function doubleClickLike() {
    if (!liked) {
      try {
        const data = await likePost(id);
        setLikes(data.likesCount);
        setLiked(true);
      } catch (err) {
        console.error("Failed to like:", err);
      }
    }
    setShowHeart(true);
    setTimeout(() => setShowHeart(false), 700);
  }

  const postForModal = {
    id,
    images,
    mediaList,
    username: author,
    avatar,
    content,
    likes,
    comments,
    likedBy: initialLikedBy,
  };

  // U PostCard.jsx, odmah pre return-a
  console.log("mediaList:", mediaList);
  console.log("current media:", mediaList?.[currentIndex]);

  return (
    <article className="ig-post_card">

      <header className="ig-post_header">
        <Link to={`/profile/${author}`} className="ig-post_user">
          <img src={avatar} alt={`${author} avatar`} className="ig-post_avatar" />
          <span className="ig-post_username">{author}</span>
        </Link>
      </header>

      <div className="ig-post_media" onDoubleClick={doubleClickLike}>
        {mediaList?.[currentIndex]?.mediaType === "VIDEO" ? (
          <video
            key={images[currentIndex]}
            src={images[currentIndex]}
            controls
            style={{ width: "100%", height: "100%", objectFit: "contain" }}
          />
        ) : (
          <img src={images[currentIndex]} alt="post" />
        )}

        {showHeart && <div className="ig-heart-animation">❤️</div>}

        {images.length > 1 && (
          <>
            {currentIndex > 0 && (
              <button className="ig-carousel_btn ig-prev" onClick={prevImage}>
                <FiChevronLeft />
              </button>
            )}
            {currentIndex < images.length - 1 && (
              <button className="ig-carousel_btn ig-next" onClick={nextImage}>
                <FiChevronRight />
              </button>
            )}
          </>
        )}
      </div>



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

      <div className="ig-post_content">
        <p className="ig-post_likes">{likes} likes</p>
        <p>
          <span className="ig-post_username">
            <Link to={`/profile/${author}`} className="ig-comment_user">{author}</Link>
          </span>{" "}
          {content}
        </p>
      </div>

      {showModal && (
        <PostModal
          post={postForModal}
          onClose={() => setShowModal(false)}
        />
      )}

    </article>
  );
}