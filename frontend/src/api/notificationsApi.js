const BASE_URL = "http://localhost:8080/api/notifications";

function getToken() {
  return localStorage.getItem("token");
}

function authHeaders() {
  return {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${getToken()}`
  };
}

export async function getNotifications(userId) {
  const response = await fetch(`${BASE_URL}/${userId}`, {
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to fetch notifications");
  return response.json();
}

export async function markAllRead(userId) {
  const response = await fetch(`${BASE_URL}/${userId}/read-all`, {
    method: "PUT",
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to mark notifications as read");
  return response.json();
}

export async function acceptFollowRequest(notificationId) {
  const response = await fetch(`${BASE_URL}/follow-request/${notificationId}/accept`, {
    method: "POST",
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to accept follow request");
  return response.json();
}

export async function declineFollowRequest(notificationId) {
  const response = await fetch(`${BASE_URL}/follow-request/${notificationId}/decline`, {
    method: "DELETE",
    headers: authHeaders()
  });
  if (!response.ok) throw new Error("Failed to decline follow request");
  return response.json();
}