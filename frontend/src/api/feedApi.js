const BASE_URL = "http://localhost:8080/api/feed";

function getToken() {
  return localStorage.getItem("token");
}

export async function getUserFeed(userId, page = 0, size = 10) {
  const response = await fetch(`${BASE_URL}/${userId}?page=${page}&size=${size}`, {
    headers: {
      "Authorization": `Bearer ${getToken()}`
    }
  });
  if (!response.ok) throw new Error("Failed to fetch feed");
  return response.json();
}