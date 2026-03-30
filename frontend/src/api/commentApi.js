const BASE_URL = "http://localhost:8080/api/comments";

function getToken() {
  return localStorage.getItem("token");
}

function authHeaders() {
  return {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${getToken()}`
  };
}

export async function addComment(postId, text) {
  const response = await fetch(`${BASE_URL}`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify({ postId, content: text })
  });
  if (!response.ok) throw new Error("Failed to add comment");
  return response.json();
}

export async function checkComment(postId) {
  const response = await fetch(`${BASE_URL}/check?postId=${postId}`, {
    method: "GET",
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to check comment status");
  return response.json();
}

export async function getPostComments(postId) {
  const response = await fetch(`${BASE_URL}/post/${postId}`, {
    method: "GET",
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to fetch comments");
  return response.json();
}

export async function deleteComment(commentId) {
  const response = await fetch(`${BASE_URL}/${commentId}`, {
    method: "DELETE",
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to delete comment");
  return response.json();
}

export async function updateComment(commentId, content) {
  const response = await fetch(`${BASE_URL}/${commentId}`, {
    method: "PATCH",
    headers: authHeaders(),
    body: JSON.stringify({ content })
  });
  if (!response.ok) throw new Error("Failed to update comment");
  return response.json();
}