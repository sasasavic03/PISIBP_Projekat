import React, { useState, useEffect } from "react";
import "./post_list.css";
import PostCard from "../post_card/PostCard";
import { getUserFeed } from "../../../api/feedApi";
import { checkLike, getPostLikesWithUsers } from "../../../api/likeApi";
import { getPostComments } from "../../../api/commentApi";

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

        const postsData = Array.isArray(data) ? data : (data && data.content) ? data.content : [];

        if (!postsData || postsData.length === 0) {
          setPosts([]);
          return;
        }

        const mapped = await Promise.all(postsData.map(async (post) => {

          const author = post.user?.username || 'Unknown User';
          const avatar = post.user?.profile_picture_url || post.user?.profilePictureUrl || '/default-avatar.jpg';

          let images = [];
          let mediaList = [];

          if (post.media_list && Array.isArray(post.media_list)) {
            images = post.media_list.map((m) => {
              const mediaUrl = m.media_url || m.mediaUrl;
              if (!mediaUrl) return "";
              return `http://localhost:8080/api/posts/media/${mediaUrl}`;
            });

            mediaList = post.media_list.map(m => ({
              mediaUrl: `http://localhost:8080/api/posts/media/${m.media_url || m.mediaUrl}`,
              mediaType: m.media_type || m.mediaType,
              orderIndex: m.order_index || m.orderIndex,
            }));
          }

          let isLiked = false;
          try {
            const likeData = await checkLike(post.id);
            isLiked = likeData.liked || false;
          } catch (err) {
            console.warn("Failed to check like status for post " + post.id, err);
          }

          let likedByData = [];
          try {
            likedByData = await getPostLikesWithUsers(post.id);
          } catch (err) {
            console.warn("Failed to fetch likes with users for post " + post.id, err);
          }

          let comments = [];
          try {
            const commentsData = await getPostComments(post.id);
            comments = commentsData?.comments ?? [];
          } catch (err) {
            console.warn("Failed to fetch comments for post " + post.id, err);
          }

          return {
            id: post.id,
            author: author,
            avatar: avatar,
            content: post.description || '',
            images: images,
            mediaList: mediaList,
            likes: post.likes_count || 0,
            liked: isLiked,
            likedBy: likedByData,
            comments: comments,
          };

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
          initialLiked={post.liked}
        />
      ))}
    </div>
  );
}