export function validateLoginForm(username, password) {
  if (!username || username.trim() === "") return { valid: false, error: "Username is required" };
  if (!password || password.trim() === "") return { valid: false, error: "Password is required" };
  return { valid: true, error: null };
}

export function validateRegisterForm(username, password, email) {
  if (!username || username.trim() === "") return { valid: false, error: "Username is required" };
  if (!password || password.length < 6) return { valid: false, error: "Password must be at least 6 characters" };
  if (!email || !email.includes("@")) return { valid: false, error: "Invalid email" };
  return { valid: true, error: null };
}

export function isLoggedIn() {
  return !!localStorage.getItem("token");
}
