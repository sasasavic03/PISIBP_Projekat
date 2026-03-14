const BASE_URL = "http://localhost:8080/api/feed";

function getToken() {
  return localStorage.getItem("token");
}

export async function getUserFeed(userId) {
  const response = await fetch(`${BASE_URL}/${userId}`, {
    headers: {
      "Authorization": `Bearer ${getToken()}`
    }
  });
  if (!response.ok) throw new Error("Failed to fetch feed");
  return response.json();
}