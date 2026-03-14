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

export async function addComment(postId, text) {
  const response = await fetch(`${BASE_URL}/${postId}/comments`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify({ text })
  });
  if (!response.ok) throw new Error("Failed to add comment");
  return response.json();
}