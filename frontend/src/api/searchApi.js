const BASE_URL = "http://localhost:8080/api/users";

function getToken() {
  return localStorage.getItem("token");
}

export async function searchUsers(query) {
  const response = await fetch(`${BASE_URL}/search?q=${encodeURIComponent(query)}`, {
    headers: {
      "Authorization": `Bearer ${getToken()}`
    }
  });
  if (!response.ok) throw new Error("Search failed");
  return response.json();
}