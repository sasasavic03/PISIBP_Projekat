import React, { useState } from "react";
import "./postmodal.css";
import{
    FiHeart,
    FiMessageSquare
}from "react-icons/fi";

export default function PostModal({ post, onClose }) {

  const [comments, setComments] = useState(post.comments);
  const [newComment, setNewComment] = useState("");
  const [likes, setLikes] = useState(post.likes);
  const [liked, setLiked] = useState(false);
  const [showHeart, setShowHeart] = useState(false);

  function addComment() {

    if (!newComment.trim()) return;

    const comment = {
      id: Date.now(),
      username: "you",
      text: newComment
    };

    setComments([...comments, comment]);
    setNewComment("");
  }

  function likePost() {

    if(liked){
      setLikes(likes - 1);
      setLiked(false);
    }else{
      setLikes(likes + 1);
      setLiked(true);
    }
  
  }

  function doubleClickLike(){

    if(!liked){
      setLikes(likes + 1);
      setLiked(true);
    }
  
    setShowHeart(true);
  
    setTimeout(()=>{
      setShowHeart(false);
    },700);
  
  }

  return (
    <div className="ig-modal-backdrop" onClick={onClose}>

      <div
        className="ig-modal"
        onClick={(e) => e.stopPropagation()}
      >
        {/* skromna verzija */}
        {/* <div className="ig-modal-image" onDoubleClick={doubleClickLike}>
          <img src={post.image} alt="post" />
        </div> */}

    <div className="ig-modal-image" onDoubleClick={doubleClickLike}>

        <img src={post.image} alt="post" />

        {showHeart && (
            <div className="ig-heart-animation">
                ❤️
            </div>
        )}

    </div>
        

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
                {liked ? <FiHeart stroke="black" fill="red"/> : <FiHeart stroke="black"/> } {likes}
              
            </button>

          </div>

          <div className="ig-modal-add-comment">

            <input
              type="text"
              placeholder="Add a comment..."
              value={newComment}
              onChange={(e)=>setNewComment(e.target.value)}
            />

            <button onClick={addComment}>
              Post
            </button>

          </div>

        </div>

      </div>

    </div>
  );
}