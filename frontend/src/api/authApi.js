export async function registerUser(payload) {

    const response = await fetch("http://localhost:8080/api/auth/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(payload)
    });
  
    if (!response.ok) {
      try {
        const errorData = await response.json();
        throw new Error(errorData.message || `Register failed with status ${response.status}`);
      } catch (e) {
        throw new Error(`Register failed with status ${response.status}: ${response.statusText}`);
      }
    }
  
    return response.json();
  }

export async function loginUser(payload) {
    const response = await fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        username: payload.username || payload.login,
        password: payload.password
      })
    });
    if (!response.ok) {
      try {
        const errorData = await response.json();
        throw new Error(errorData.message || `Login failed with status ${response.status}`);
      } catch (e) {
        throw new Error(`Login failed with status ${response.status}: ${response.statusText}`);
      }
    }
    return response.json();
  }