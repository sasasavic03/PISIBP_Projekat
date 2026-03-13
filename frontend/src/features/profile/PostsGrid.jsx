import React, { useState, useEffect, useRef } from "react";
import PostModal from "../posts/post_modal/PostModal";
import "./postsgrid.css";
import{
    FiHeart,
    FiMessageSquare
}from "react-icons/fi";

export default function PostsGrid({ posts: initialPosts, isOwnProfile }) {

  const [posts, setPosts] = useState(initialPosts);
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

  function handleDeletePost(postId) {
    const updated = posts.filter(p => p.id !== postId);
    setPosts(updated);
    setSelectedPost(null);
  }

  function handleDeleteImage(postId, imageIndex) {
    const updated = posts.map(p => {
      if (p.id !== postId) return p;
      const updatedImages = p.images.filter((_, i) => i !== imageIndex);
      return { ...p, images: updatedImages, image: updatedImages[0] };
    });
    setPosts(updated);
    // azuriraj selectedPost da carousel odmah reflektuje promenu
    const updatedPost = updated.find(p => p.id === postId);
    setSelectedPost(updatedPost);
  }



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
          isOwner={isOwnProfile}
          onDeletePost={handleDeletePost}
          onDeleteImage={handleDeleteImage}
        />
      )}
</>
    
  );
}