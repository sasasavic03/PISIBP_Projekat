import React, { useState, useEffect, useRef } from "react";
import PostModal from "../posts/post_modal/PostModal";
import "./postsgrid.css";
import { FiHeart, FiMessageSquare } from "react-icons/fi";
import { getUserPosts } from "../../api/postApi";
import { deletePost, deletePostMedia } from "../../api/postApi";

export default function PostsGrid({ username, isOwnProfile }) {

  const [posts, setPosts] = useState([]);
  const [selectedPost, setSelectedPost] = useState(null);
  const [visiblePosts, setVisiblePosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loaderRef = useRef(null);
  const POSTS_PER_LOAD = 6;

  useEffect(() => {
    async function fetchPosts() {
      setLoading(true);
      setError(null);
      try {
        const data = await getUserPosts(username);
        const mapped = data.map(post => ({
          id: post.id,
          image: post.image,
          images: post.images,
          likes: post.likesCount,
          likedBy: post.likedBy ?? [],
          comments: post.comments ?? [],
          content: post.description,
        }));
        setPosts(mapped);
      } catch (err) {
        setError(err.message);
        console.error("Failed to fetch posts:", err);
      } finally {
        setLoading(false);
      }
    }

    if (username) fetchPosts();
  }, [username]);

  useEffect(() => {
    setVisiblePosts(posts.slice(0, POSTS_PER_LOAD));
  }, [posts]);

  function loadMorePosts() {
    const currentLength = visiblePosts.length;
    const nextPosts = posts.slice(currentLength, currentLength + POSTS_PER_LOAD);
    setVisiblePosts([...visiblePosts, ...nextPosts]);
  }

  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting) loadMorePosts();
      },
      { threshold: 1 }
    );
    if (loaderRef.current) observer.observe(loaderRef.current);
    return () => observer.disconnect();
  }, [visiblePosts]);

  async function handleDeletePost(postId) {
    try {
      await deletePost(postId);
      const updated = posts.filter(p => p.id !== postId);
      setPosts(updated);
      setSelectedPost(null);
    } catch (err) {
      console.error("Failed to delete post:", err);
    }
  }

  async function handleDeleteImage(postId, imageIndex) {
    try {
      await deletePostMedia(postId, imageIndex);
      const updated = posts.map(p => {
        if (p.id !== postId) return p;
        const updatedImages = p.images.filter((_, i) => i !== imageIndex);
        return { ...p, images: updatedImages, image: updatedImages[0] };
      });
      setPosts(updated);
      const updatedPost = updated.find(p => p.id === postId);
      setSelectedPost(updatedPost);
    } catch (err) {
      console.error("Failed to delete image:", err);
    }
  }

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;
  if (posts.length === 0) return <div>No posts yet.</div>;

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
              <span><FiHeart fill="white" /> {post.likes}</span>
              <span><FiMessageSquare fill="white" /> {post.comments.length}</span>
            </div>
          </div>
        ))}
      </div>

      <div ref={loaderRef} style={{ height: "40px" }} />

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