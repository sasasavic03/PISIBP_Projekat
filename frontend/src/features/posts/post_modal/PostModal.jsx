import React, { useState } from "react";
import "./postmodal.css";
import { FiHeart, FiChevronLeft, FiChevronRight } from "react-icons/fi";

export default function PostModal({ post, onClose }) {

  const [comments, setComments] = useState(post.comments);
  const [newComment, setNewComment] = useState("");
  const [likes, setLikes] = useState(post.likes);
  const [liked, setLiked] = useState(false);
  const [showHeart, setShowHeart] = useState(false);
  const [currentIndex, setCurrentIndex] = useState(0);

  const images = post.images ?? [post.image];

  function nextImage() {
    setCurrentIndex((prev) => prev === images.length - 1 ? prev : prev + 1);
  }

  function prevImage() {
    setCurrentIndex((prev) => prev === 0 ? prev : prev - 1);
  }

  function likePost() {
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

  function addComment() {
    if (!newComment.trim()) return;
    const comment = { id: Date.now(), username: "you", text: newComment };
    setComments([...comments, comment]);
    setNewComment("");
  }

  return (
    <div className="ig-modal-backdrop" onClick={onClose}>
      <div className="ig-modal" onClick={(e) => e.stopPropagation()}>

        {/* leva strana - slika */}
        <div className="ig-modal-image" onDoubleClick={doubleClickLike}>

          <img src={images[currentIndex]} alt="post" />

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

          {images.length > 1 && (
            <div className="ig-carousel_dots">
              {images.map((_, i) => (
                <span key={i} className={`ig-carousel_dot${i === currentIndex ? " active" : ""}`} />
              ))}
            </div>
          )}

        </div>

        {/* desna strana */}
        <div className="ig-modal-side">

          <div className="ig-modal-header">
            <strong>{post.username}</strong>
          </div>

          <div className="ig-modal-comments">
            {comments.map((c) => (
              <div key={c.id} className="ig-comment">
                <strong>{c.username}</strong> {c.text}
              </div>
            ))}
          </div>

          <div className="ig-modal-actions">
            <button onClick={likePost}>
              {liked
                ? <FiHeart stroke="black" fill="red" />
                : <FiHeart stroke="black" />
              } {likes}
            </button>
          </div>

          <div className="ig-modal-add-comment">
            <input
              type="text"
              placeholder="Add a comment..."
              value={newComment}
              onChange={(e) => setNewComment(e.target.value)}
              onKeyDown={(e) => e.key === "Enter" && addComment()}
            />
            <button onClick={addComment}>Post</button>
          </div>

        </div>

      </div>
    </div>
  );
}