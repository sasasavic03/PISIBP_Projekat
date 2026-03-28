const BASE_URL = "http://localhost:8080/api/follow";

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
  const followerId = localStorage.getItem("userId");
  const response = await fetch(`${BASE_URL}/${targetUserId}`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify({
      followerId: followerId,
      private: isPrivate
    })
  });
  if (!response.ok) throw new Error("Failed to follow user");
  return response.json();
}

export async function unfollowUser(targetUserId) {
  const followerId = localStorage.getItem("userId");
  const response = await fetch(`${BASE_URL}/${targetUserId}`, {
    method: "DELETE",
    headers: authHeaders(),
    body: JSON.stringify({
      followerId: followerId
    })
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