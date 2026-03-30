const BASE_URL = "http://localhost:8080/api/follows";

function getToken() {
  return localStorage.getItem("token");
}

function authHeaders() {
  return {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${getToken()}`
  };
}

export async function followUser(targetUserId, isPrivate = false) {
  const response = await fetch(`${BASE_URL}/${targetUserId}`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify({
      private: isPrivate
    })
  });
  if (!response.ok) throw new Error("Failed to follow user");
  return response.json();
}

export async function unfollowUser(targetUserId) {
  const response = await fetch(`${BASE_URL}/${targetUserId}`, {
    method: "DELETE",
    headers: authHeaders(),
  });
  if (!response.ok) throw new Error("Failed to unfollow user");
  return response.json();
}

export async function getFollowers(userId) {
  const response = await fetch(`${BASE_URL}/${userId}/followers`, {
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to fetch followers");
  return response.json();
}

export async function getFollowing(userId) {
  const response = await fetch(`${BASE_URL}/${userId}/following`, {
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to fetch following");
  return response.json();
}

/* export async function checkFollow(followingId) {
  const response = await fetch(`${BASE_URL}/check?followingId=${followingId}`, {
    method: "GET",
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to check follow status");
  return response.json();
} */

export async function checkFollow(targetUserId) {
  const response = await fetch(`${BASE_URL}/${targetUserId}/status`, {
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to check follow status");
  return response.json();
}

/* potencijalno nepotrebno */
export async function checkComment(postId) {
  const response = await fetch(`${BASE_URL}/check?postId=${postId}`, {
    method: "GET",
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to check comment status");
  return response.json();
}