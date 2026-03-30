import React, { useState, useEffect, useRef } from "react";
import PostModal from "../posts/post_modal/PostModal";
import "./postsgrid.css";
import { FiHeart, FiMessageSquare } from "react-icons/fi";
import { getUserPosts } from "../../api/postApi";
import { getUserProfile } from "../../api/userApi";
import { deletePost, deletePostMedia } from "../../api/postApi";
import { getPostComments } from "../../api/commentApi";

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
        const userProfile = await getUserProfile(username);
        const userId = userProfile.id;


        const data = await getUserPosts(userId);

        const mapped = await Promise.all(data.map(async (post) => {
              try {
                  
                  const commentsData = await getPostComments(post.id);
                  const commentCount = commentsData?.count || 0;

                  return {
                      id: post.id,
                      image: `http://localhost:8080/api/posts/media/${post.media_list?.[0]?.media_url}`,
                      images: post.media_list.map(m => `http://localhost:8080/api/posts/media/${m.media_url}`),
                      mediaList: post.media_list.map(m => ({
                          mediaUrl: `http://localhost:8080/api/posts/media/${m.media_url}`,
                          mediaType: m.media_type,
                          orderIndex: m.order_index,
                      })),
                      likes: post.likes_count,
                      likedBy: post.likedBy ?? [],
                      comments: commentsData?.comments ?? [],
                      commentCount: commentCount,
                      content: post.description,
                      username: post.user?.username,
                      avatar: post.user?.profilePictureUrl,
                  };
              } catch (err) {
                  console.error(`Failed to fetch comments for post ${post.id}:`, err);

                  return {
                      id: post.id,
                      image: `http://localhost:8080/api/posts/media/${post.media_list?.[0]?.media_url}`,
                      images: post.media_list.map(m => `http://localhost:8080/api/posts/media/${m.media_url}`),
                      mediaList: post.media_list.map(m => ({
                          mediaUrl: `http://localhost:8080/api/posts/media/${m.media_url}`,
                          mediaType: m.media_type,
                          orderIndex: m.order_index,
                      })),
                      likes: post.likes_count,
                      likedBy: post.likedBy ?? [],
                      comments: [],
                      commentCount: 0,
                      content: post.description,
                      username: post.user?.username,
                      avatar: post.user?.profilePictureUrl,
                  };
              }
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

  function constructMediaUrl(mediaUrl) {
    if (!mediaUrl) return null;
    if (mediaUrl.startsWith('http')) {
      return mediaUrl;
    }
    return `http://localhost:8080/api/posts/media/${mediaUrl}`;
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
        {post.mediaList?.[0]?.mediaType === "VIDEO" ? (
              <video
                src={post.image}
                style={{ width: "100%", height: "100%", objectFit: "cover" }}
                preload="metadata"
              />
            ) : (        
            <img src={post.image} alt="post" />
            )}
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