import React from "react";
import { Link } from "react-router"
import "./sidebar.css";
import {
  FiHome,
  FiSearch,
  FiCompass,
  FiSend,
  FiHeart,
  FiPlusSquare,
  FiUser,
} from "react-icons/fi";

const navItems = [
  { key: "home", label: "Home", icon: <FiHome />, href: "/home"},
  { key: "search", label: "Search", icon: <FiSearch />, href: "/search" },
  { key: "explore", label: "Explore", icon: <FiCompass />, href: "/explore" },
  { key: "messages", label: "Messages", icon: <FiSend />, href: "/messages" },
  { key: "notifications", label: "Notifications", icon: <FiHeart />, href: "/notifications" },
  { key: "create", label: "Create", icon: <FiPlusSquare />, href: "/create" },
  { key: "profile", label: "Profile", icon: <FiUser />, href: "/profiletest" },
];

export default function Sidebar({ activeKey = "home", onNavigate, showSearch, onSearchOpen, onSearchClose, onCreateOpen, showNotifications, onNotificationsOpen, onNotificationsClose, unreadCount }) {
  

  console.log("unreadCount:", unreadCount);

  const handleClick = (e, item) => {
    if (onNavigate) {
      e.preventDefault();
      onNavigate(item.key);
    }
  };

  return (
    <aside className="ig-sidebar" aria-label="Sidebar navigation">
      <div className="ig-sidebar__inner">

        <div className="ig-sidebar__logo" aria-label="Instagram">
          <span className="ig-sidebar__logoMark">Instagram</span>
        </div>

        <nav className="ig-sidebar__nav" aria-label="Primary">
          {navItems.map((item) => {
            const isActive = item.key === activeKey;
            const isSearch = item.key === "search";
            const isCreate = item.key === "create";
            const isNotifications = item.key === "notifications";

            if (isSearch) {
              return (
                <button
                  key={item.key}
                  className={`ig-sidebar__link ${showSearch ? "is-active" : ""}`}
                  onClick={() => showSearch ? onSearchClose() : onSearchOpen()}
                >
                  <span className="ig-sidebar__icon" aria-hidden="true">
                    <FiSearch />
                  </span>
                  <span className="ig-sidebar__label">Search</span>
                </button>
              );
            }

            if (isCreate) {
              return (
                <button
                  key={item.key}
                  className="ig-sidebar__link"
                  onClick={onCreateOpen}
                >
                  <span className="ig-sidebar__icon"><FiPlusSquare /></span>
                  <span className="ig-sidebar__label">Create</span>
                </button>
              );
            }

            if (isNotifications) {
              return (
                <button
                  key={item.key}
                  className={`ig-sidebar__link ${showNotifications ? "is-active" : ""}`}
                  onClick={() => showNotifications ? onNotificationsClose() : onNotificationsOpen()}
                >
                  <span className="ig-sidebar__icon" style={{ position: "relative" }}>
                    <FiHeart />
                    {unreadCount > 0 && (
                      <span className="ig-sidebar__badge" />
                    )}
                  </span>
                  <span className="ig-sidebar__label">Notifications</span>
                </button>
              );
            }


            return (
              <Link
                key={item.key}
                className={`ig-sidebar__link ${isActive ? "is-active" : ""}`}
                to={item.href}
                onClick={(e) => handleClick(e, item)}
                aria-current={isActive ? "page" : undefined}
              >
                <span className="ig-sidebar__icon" aria-hidden="true"> {item.icon}</span>
                <span className="ig-sidebar__label">{item.label}</span>
              </Link>
            );
          })}
        </nav>

      </div>
    </aside>
  );
}
