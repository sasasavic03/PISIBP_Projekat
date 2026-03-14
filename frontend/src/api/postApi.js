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

export async function getUserPosts(username) {
  const response = await fetch(`${BASE_URL}/user/${username}`, {
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to fetch posts");
  return response.json();
}

export async function createPost(files, description) {
  const formData = new FormData();
  files.forEach(f => formData.append("files[]", f.file));
  formData.append("description", description);
  const response = await fetch(`${BASE_URL}`, {
    method: "POST",
    headers: { "Authorization": `Bearer ${getToken()}` },
    body: formData
  });
  if (!response.ok) throw new Error("Failed to create post");
  return response.json();
}

export async function deletePost(postId) {
  const response = await fetch(`${BASE_URL}/${postId}`, {
    method: "DELETE",
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to delete post");
  return response.json();
}

export async function deletePostMedia(postId, mediaIndex) {
  const response = await fetch(`${BASE_URL}/${postId}/media/${mediaIndex}`, {
    method: "DELETE",
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to delete media");
  return response.json();
}