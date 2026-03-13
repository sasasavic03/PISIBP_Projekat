import React, { useState } from "react";
import "./postmodal.css";
import { FiHeart, FiChevronLeft, FiChevronRight } from "react-icons/fi";
import LikedByModal from "../likedby_modal/LikeByModal";
import {Link} from "react-router-dom";

export default function PostModal({ post, onClose,isOwner, onDeletePost, onDeleteImage  }) {

  const [comments, setComments] = useState(post.comments);
  const [newComment, setNewComment] = useState("");
  const [likes, setLikes] = useState(post.likes);
  const [liked, setLiked] = useState(false);
  const [showHeart, setShowHeart] = useState(false);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [likedBy, setLikedBy] = useState(post.likedBy ?? []);
  const [showLikedBy, setShowLikedBy] = useState(false);
  const [showOptions, setShowOptions] = useState(false);

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
      setLikedBy(likedBy.filter(u => u.username !== "you"));
    } else {
      setLikes(likes + 1);
      setLiked(true);
      setLikedBy([{ id: 0, username: "you", avatar: "https://i.pravatar.cc/150?img=10" }, ...likedBy]);
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

            {isOwner && (
              <button className="ig-modal-options-btn" onClick={() => setShowOptions(true)}>
                ···
              </button>
            )}
          </div>

          <div className="ig-modal-comments">

            {/* caption */}
            {post.content && (
              <div className="ig-comment">
                <img src={post.avatar} alt={post.username} className="ig-comment-avatar" />
                <div className="ig-comment-body">
                  <strong>
                    <Link
                      to={`/profile/${post.username}`}
                      onClick={(e) => e.stopPropagation()}
                      className="ig-comment_profile"
                    >
                      {post.username}
                    </Link>
                  </strong>{" "}
                  {post.content}
                </div>
              </div>
            )}
            
            {comments.map((c) => (
              <div key={c.id} className="ig-comment">
                <Link to={`/profile/${c.username}`} onClick={(e) => e.stopPropagation()}>
                    <img src={c.avatar} alt={c.username} className="ig-comment-avatar" />
                </Link>
                

              <div className="ig-comment-body">
                <strong><Link 
                              to={`/profile/${c.username}`}
                              onClick={(e) => e.stopPropagation()} className="ig-comment_profile" >
                              {c.username}
                        </Link>
                  </strong> {c.text}
              </div>
                
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

          {likedBy.length > 0 && (
            <div className="ig-modal-liked-by">
              <div className="ig-modal-liked-avatars">
                {likedBy.slice(0, 3).map((u) => (
                  <img key={u.id} src={u.avatar} alt={u.username} title={u.username} />
                ))}
              </div>
              <span className="ig-modal-liked-text">
                Liked by <strong>{likedBy[0].username}</strong>
                {likedBy.length > 1 && (
                  <>
                    {" "}and{" "}
                    <strong
                      className="ig-liked-by-others"
                      onClick={() => setShowLikedBy(true)}
                    >
                      {likedBy.length - 1} others
                    </strong>
                  </>
                )}
              </span>
            </div>
          )}

          {showLikedBy && (
            <LikedByModal
              likedBy={likedBy}
              onClose={() => setShowLikedBy(false)}
            />
          )}

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

      {showOptions && (
          <div className="ig-post_options_backdrop" onClick={() => setShowOptions(false)}>
            <div className="ig-post_options_panel" onClick={e => e.stopPropagation()}>

              <button
                className="ig-post_options_item ig-post_options_danger"
                onClick={() => onDeletePost(post.id)}
              >
                Delete post
              </button>
              <div className="ig-post_options_divider" />

              {images.length > 1 && (
                <>
                  <button
                    className="ig-post_options_item ig-post_options_danger"
                    onClick={() => {
                      onDeleteImage(post.id, currentIndex);
                      setShowOptions(false);
                    }}
                  >
                    Delete current photo
                  </button>
                  <div className="ig-post_options_divider" />
                </>
              )}

              <button
                className="ig-post_options_item"
                onClick={() => setShowOptions(false)}
              >
                Cancel
              </button>

            </div>
          </div>
        )}
    </div>
  );
}