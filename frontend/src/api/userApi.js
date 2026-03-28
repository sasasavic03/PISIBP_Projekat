const BASE_URL = "http://localhost:8080/api/users";

function getToken() {
  return localStorage.getItem("token");
}

function authHeaders() {
  return {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${getToken()}`
  };
}

export async function getUserProfile(username) {
  const response = await fetch(`${BASE_URL}/username/${username}`, {
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to fetch profile");
  return response.json();
}

export async function getUserStats(username) {
  const response = await fetch(`${BASE_URL}/username/${username}/stats`, {
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to fetch stats");
  return response.json();
}

export async function updateUserSettings(userId, payload) {
  const response = await fetch(`${BASE_URL}/${userId}/settings`, {
    method: "PUT",
    headers: authHeaders(),
    body: JSON.stringify(payload)
  });
  if (!response.ok) throw new Error("Failed to update settings");
  return response.json();
}

export async function updateAvatar(userId, file) {
  const formData = new FormData();
  formData.append("file", file);
  const response = await fetch(`${BASE_URL}/${userId}/avatar`, {
    method: "PUT",
    headers: { "Authorization": `Bearer ${getToken()}` },
    body: formData
  });
  if (!response.ok) throw new Error("Failed to update avatar");
  return response.json();
}

export async function getBlockedUsers(userId) {
  const response = await fetch(`${BASE_URL}/${userId}/blocked`, {
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to fetch blocked users");
  return response.json();
}

export async function blockUser(targetUserId) {
  const response = await fetch(`${BASE_URL}/${targetUserId}/block`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify({
      blockedId: targetUserId
    })
  });
  if (!response.ok) throw new Error("Failed to block user");
  return response.json();
}

export async function unblockUser(targetUserId) {
  const response = await fetch(`${BASE_URL}/${targetUserId}/block`, {
    method: "DELETE",
    headers: authHeaders(),
    body: JSON.stringify({
      blockedId: targetUserId
    })
  });
  if (!response.ok) throw new Error("Failed to unblock user");
  return response.json();
}

export async function getSuggestions() {
  const response = await fetch(`${BASE_URL}/suggestions`, {
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to fetch suggestions");
  return response.json();
}