import React, { useState } from "react";
import "./settings.css";

const blockedMock = [
  { id: 1, username: "john.doe",   avatar: "https://i.pravatar.cc/150?img=1" },
  { id: 2, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=2" },
  { id: 3, username: "mike.ross",  avatar: "https://i.pravatar.cc/150?img=3" },
];

export default function Settings() {

  const [form, setForm] = useState({
    avatar: "",
    username: "john.doe",
    fullName: "John Doe",
    email: "john.doe@gmail.com",
    bio: "📸 Photography enthusiast | 🌍 Traveler | ☕ Coffee addict",
    password: "",
    confirmPassword: "",
  });

  const [preview, setPreview] = useState("https://i.pravatar.cc/150?img=1");
  const [saved, setSaved] = useState(false);
  const [blocked, setBlocked] = useState(blockedMock);

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  function handleAvatarChange(e) {
    const file = e.target.files[0];
    if (file) setPreview(URL.createObjectURL(file));
  }

  function handleSubmit(e) {
    e.preventDefault();
    setSaved(true);
    setTimeout(() => setSaved(false), 3000);
  }

  function unblock(id) {
    setBlocked(blocked.filter(u => u.id !== id));
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
    {saved && <span className="ig-settings-saved">✓ Changes saved</span>}
    <button type="submit" className="ig-settings-submit">Save changes</button>
  </div>

</div>

</form>
      </div>

      
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
                <button
                  className="ig-settings-unblock-btn"
                  onClick={() => unblock(user.id)}
                >
                  Unblock
                </button>
              </li>
            ))}
          </ul>
        )}

      </div>

    </div>
  );
}