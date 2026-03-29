import React, { useState, useEffect } from "react";
import "./post_list.css";
import PostCard from "../post_card/PostCard";
import { getUserFeed } from "../../../api/feedApi";

export default function PostList() {

  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const userId = parseInt(localStorage.getItem("userId")) || 1;

  useEffect(() => {
    async function fetchFeed() {
      setLoading(true);
      setError(null);

      try {
        const data = await getUserFeed(userId);
        
        
        const mapped = data.map(post => ({
          id: post.id,
          author: post.authorUsername,
          avatar: post.authorAvatar,
          content: post.description,
          images: post.mediaList.map(m => m.mediaUrl),
          mediaList: post.mediaList,
          likes: post.likesCount,
          likedBy: post.likedBy ?? [],
          comments: post.comments ?? [],
        }));

        setPosts(mapped);
      } catch (err) {
        setError(err.message);
        console.error("Failed to fetch feed:", err);
      } finally {
        setLoading(false);
      }
    }

    fetchFeed();
  }, [userId]);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;
  if (posts.length === 0) return <div>No posts yet.</div>;

  return (
    <div className="post-list">
      {posts.map((post) => (
        <PostCard
          key={post.id}
          id={post.id} 
          mediaList={post.mediaList} 
          author={post.author}
          content={post.content}
          avatar={post.avatar}
          images={post.images}
          likes={post.likes}
          comments={post.comments}
          likedBy={post.likedBy}
        />
      ))}
    </div>
  );
}