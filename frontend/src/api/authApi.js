export async function registerUser(payload) {

    const response = await fetch("http://localhost:8080/api/auth/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(payload)
    });
  
    if (!response.ok) {
      throw new Error("Register failed");
    }
  
    return response.json();
  }

  export async function loginUser(payload) {
    const response = await fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });
    if (!response.ok) throw new Error("Login failed");
    return response.json();
  }