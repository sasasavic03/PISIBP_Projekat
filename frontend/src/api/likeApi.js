const BASE_URL = "http://localhost:8080/api/likes";

function getToken() {
  return localStorage.getItem("token");
}

function authHeaders() {
  return {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${getToken()}`
  };
}

export async function likePost(postId) {
  const response = await fetch(`${BASE_URL}`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify({ postId })
  });
  if (!response.ok) throw new Error("Failed to like post");
  return response.json();
}

export async function unlikePost(postId) {
  const response = await fetch(`${BASE_URL}`, {
    method: "DELETE",
    headers: authHeaders(),
    body: JSON.stringify({ postId })
  });
  if (!response.ok) throw new Error("Failed to unlike post");
  return response.json();
}

export async function checkLike(postId) {
  const response = await fetch(`${BASE_URL}/check?postId=${postId}`, {
    method: "GET",
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to check like status");
  return response.json();
}

export async function getPostLikesWithUsers(postId) {
  const response = await fetch(`${BASE_URL}/post/${postId}/with-users`, {
    method: "GET",
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to fetch post likes");
  return response.json();
}