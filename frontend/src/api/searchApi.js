const BASE_URL = "http://localhost:8080/api/users";

function getToken() {
  return localStorage.getItem("token");
}

export async function searchUsers(query) {
  const response = await fetch(`${BASE_URL}/search?q=${encodeURIComponent(query)}`, {
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${getToken()}`
    }
  });
  if (!response.ok) throw new Error("Search failed");
  const data = await response.json();
  return data.content;
}