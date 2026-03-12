import React, { useState, useEffect, useRef } from "react";
import PostModal from "../posts/post_modal/PostModal";
import "./postsgrid.css";
import{
    FiHeart,
    FiMessageSquare
}from "react-icons/fi";

export default function PostsGrid({ posts }) {

  const [selectedPost, setSelectedPost] = useState(null);
  const [visiblePosts, setVisiblePosts] = useState([]);

  const loaderRef = useRef(null);

  const POSTS_PER_LOAD = 6;

  useEffect(() => {

    setVisiblePosts(posts.slice(0, POSTS_PER_LOAD));

  }, [posts]);

  function loadMorePosts(){

    const currentLength = visiblePosts.length;

    const nextPosts = posts.slice(
      currentLength,
      currentLength + POSTS_PER_LOAD
    );

    setVisiblePosts([...visiblePosts, ...nextPosts]);

  }

  useEffect(()=>{

    const observer = new IntersectionObserver(
      (entries)=>{

        if(entries[0].isIntersecting){
          loadMorePosts();
        }

      },
      {threshold:1}
    );

    if(loaderRef.current){
      observer.observe(loaderRef.current);
    }

    return ()=>observer.disconnect();

  },[visiblePosts]);




  return (
    <>
    <div className="ig-posts-grid">

      {visiblePosts.map((post) => (
        <div
          key={post.id}
          className="ig-post-item"
          onClick={() => setSelectedPost(post)}
        >

          <img src={post.image} alt="post" />

          <div className="ig-post-overlay">
            <span><FiHeart fill="white"/> {post.likes}</span>
            <span><FiMessageSquare fill="white"/> {post.comments.length}</span>
          </div>

        </div>
      ))}

    </div>

        <div ref={loaderRef} style={{height:"40px"}} />

      {selectedPost && (
        <PostModal
          post={selectedPost}
          onClose={() => setSelectedPost(null)}
        />
      )}
</>
    
  );
}