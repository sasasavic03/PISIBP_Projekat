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