const BASE_URL = "http://localhost:8080/api/posts";

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
  const response = await fetch(`${BASE_URL}/${postId}/like`, {
    method: "POST",
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to like post");
  return response.json();
}

export async function unlikePost(postId) {
  const response = await fetch(`${BASE_URL}/${postId}/like`, {
    method: "DELETE",
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to unlike post");
  return response.json();
}