import React, { useState } from "react";
import { Link } from "react-router-dom";
import "./notificationspanel.css";


export default function NotificationsPanel({notifications, setNotifications, onClose }) {

 

  function accept(id) {
    setNotifications(notifications.map(n =>
      n.id === id
        ? { ...n, type: "follow_accepted_by_you", message: "You accepted their request.", read: true }
        : n
    ));
  }

  function decline(id) {
    setNotifications(notifications.filter(n => n.id !== id));
  }

  function markAllRead() {
    setNotifications(notifications.map(n => ({ ...n, read: true })));
  }

  const unreadCount = notifications.filter(n => !n.read).length;

  return (
    <div className="ig-notif-panel">

      <div className="ig-notif-header">
        <h2>Notifications</h2>
        {unreadCount > 0 && (
          <button className="ig-notif-markread" onClick={markAllRead}>
            Mark all as read
          </button>
        )}
      </div>

      <div className="ig-notif-divider" />

      <div className="ig-notif-list">

        {notifications.length === 0 ? (
          <p className="ig-notif-empty">No notifications.</p>
        ) : (
          notifications.map((n) => (
            <div
              key={n.id}
              className={`ig-notif-item ${!n.read ? "unread" : ""}`}
            >
              <Link
                to={`/profile/${n.username}`}
                onClick={onClose}
                className="ig-notif-avatar-link"
              >
                <img src={n.avatar} alt={n.username} />
              </Link>

              <div className="ig-notif-content">
                <p className="ig-notif-text">
                  <Link
                    to={`/profile/${n.username}`}
                    onClick={onClose}
                    className="ig-notif-username"
                  >
                    {n.username}
                  </Link>{" "}
                  {n.message}
                </p>
                <span className="ig-notif-time">{n.time}</span>
              </div>

              {n.type === "follow_request" && (
                <div className="ig-notif-actions">
                  <button
                    className="ig-notif-accept"
                    onClick={() => accept(n.id)}
                  >
                    Accept
                  </button>
                  <button
                    className="ig-notif-decline"
                    onClick={() => decline(n.id)}
                  >
                    Decline
                  </button>
                </div>
              )}

            </div>
          ))
        )}

      </div>

    </div>
  );
}