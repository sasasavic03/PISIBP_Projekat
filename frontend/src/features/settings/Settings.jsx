import React, { useState, useEffect } from "react";
import "./settings.css";
import { updateUserSettings, updateAvatar, getBlockedUsers, unblockUser, updatePrivacy } from "../../api/userApi";

export default function Settings() {

  const loggedUserId = parseInt(localStorage.getItem("userId")) || 1;

  const [form, setForm] = useState({
    username: localStorage.getItem("username") || "",
    fullName: "",
    email: "",
    bio: "",
    password: "",
    confirmPassword: "",
  });

  const [preview, setPreview] = useState(localStorage.getItem("avatar") || "");
  const [avatarFile, setAvatarFile] = useState(null);
  const [saved, setSaved] = useState(false);
  const [error, setError] = useState(null);
  const [blocked, setBlocked] = useState([]);
  const [isPrivate, setIsPrivate] = useState(false);

  useEffect(() => {
    async function fetchBlocked() {
      try {
        const data = await getBlockedUsers(loggedUserId);
        setBlocked(data);
      } catch (err) {
        console.error("Failed to fetch blocked users:", err);
      }
    }
    fetchBlocked();
  }, []);

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  function handleAvatarChange(e) {
    const file = e.target.files[0];
    if (file) {
      setAvatarFile(file);
      setPreview(URL.createObjectURL(file));
    }
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError(null);

    if (form.password && form.password !== form.confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    try {
      
      if (avatarFile) {
        const avatarData = await updateAvatar(loggedUserId, avatarFile);
        localStorage.setItem("avatar", avatarData.avatarUrl);
      }

      
      const payload = {
        username: form.username,
        fullName: form.fullName,
        email: form.email,
        bio: form.bio,
      };

      if (form.password) {
        payload.password = form.password;
      }

      await updateUserSettings(loggedUserId, payload);

      localStorage.setItem("username", form.username);

      setSaved(true);
      setTimeout(() => setSaved(false), 3000);
    } catch (err) {
      setError("Failed to save changes.");
      console.error("Settings update failed:", err);
    }
  }

  async function handleUnblock(userId) {
    try {
      await unblockUser(userId);
      setBlocked(blocked.filter(u => u.id !== userId));
    } catch (err) {
      console.error("Failed to unblock:", err);
    }
  }

  async function handlePrivacyToggle() {
    const newValue = !isPrivate;
    try {
      setIsPrivate(newValue);
      await updatePrivacy(loggedUserId, newValue);
    } catch (err) {
      console.error("Failed to update privacy:", err);
      setIsPrivate(isPrivate); // vrati na staro ako ne uspe
    }
  }

  return (
    <div className="ig-settings-layout">
  
      <div className="ig-settings">
  
        <div className="ig-settings-header">
          <h2>Edit profile</h2>
        </div>
  
        <form className="ig-settings-form" onSubmit={handleSubmit}>
  
          <div className="ig-settings-avatar-row">
            <img src={preview} alt="avatar" className="ig-settings-avatar" />
            <div className="ig-settings-avatar-info">
              <span className="ig-settings-username">{form.username}</span>
              <label className="ig-settings-avatar-btn">
                Change photo
                <input type="file" accept="image/*" onChange={handleAvatarChange} hidden />
              </label>
            </div>
          </div>
  
          <div className="ig-settings-divider" />
  
          <div className="ig-settings-grid">
  
            <div className="ig-settings-row">
              <label>Username</label>
              <input type="text" name="username" value={form.username} onChange={handleChange} />
            </div>
  
            <div className="ig-settings-row">
              <label>Full name</label>
              <input type="text" name="fullName" value={form.fullName} onChange={handleChange} />
            </div>
  
            <div className="ig-settings-divider" />
  
            <div className="ig-settings-row">
              <label>Email</label>
              <input type="email" name="email" value={form.email} onChange={handleChange} />
            </div>
  
            <div className="ig-settings-row ig-settings-full">
              <label>Bio</label>
              <textarea name="bio" value={form.bio} onChange={handleChange} rows={3} maxLength={150} />
              <span className="ig-settings-charcount">{form.bio.length}/150</span>
            </div>
  
            <div className="ig-settings-divider" />
  
            <div className="ig-settings-row">
              <label>New password</label>
              <input type="password" name="password" value={form.password} onChange={handleChange} placeholder="Leave blank to keep current" />
            </div>
  
            <div className="ig-settings-row">
              <label>Confirm password</label>
              <input type="password" name="confirmPassword" value={form.confirmPassword} onChange={handleChange} placeholder="Repeat new password" />
            </div>
  
            <div className="ig-settings-footer">
              {error && <span className="ig-settings-error">{error}</span>}
              {saved && <span className="ig-settings-saved">✓ Changes saved</span>}
              <button type="submit" className="ig-settings-submit">Save changes</button>
            </div>
  
          </div>
  
        </form>
      </div>
  
      <div className="ig-settings-right-col">
  
        <div className="ig-settings-blocked">
          <div className="ig-settings-header">
            <h2>Blocked accounts</h2>
          </div>
          {blocked.length === 0 ? (
            <p className="ig-settings-blocked-empty">No blocked accounts.</p>
          ) : (
            <ul className="ig-settings-blocked-list">
              {blocked.map((user) => (
                <li key={user.id} className="ig-settings-blocked-item">
                  <img src={user.avatar} alt={user.username} />
                  <span className="ig-settings-blocked-username">{user.username}</span>
                  <button className="ig-settings-unblock-btn" onClick={() => handleUnblock(user.id)}>
                    Unblock
                  </button>
                </li>
              ))}
            </ul>
          )}
        </div>
  
        <div className="ig-settings-blocked">
          <div className="ig-settings-header">
            <h2>Account privacy</h2>
          </div>
          <div className="ig-settings-privacy">
            <div className="ig-settings-privacy-info">
              <span className="ig-settings-privacy-title">
                {isPrivate ? "Private account" : "Public account"}
              </span>
              <span className="ig-settings-privacy-desc">
                {isPrivate
                  ? "Only people you approve can see your photos and videos."
                  : "Anyone can see your photos and videos."
                }
              </span>
            </div>
            <label className="ig-settings-toggle">
              <input type="checkbox" checked={isPrivate} onChange={handlePrivacyToggle} />
              <span className="ig-settings-toggle-slider" />
            </label>
          </div>
        </div>
  
      </div>
  
    </div>
  );
}