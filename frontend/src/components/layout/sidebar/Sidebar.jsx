import React from "react";
import "./sidebar.css";
import { Link, useLocation,useNavigate} from "react-router-dom";
import {
  FiHome,
  FiSearch,
  FiCompass,
  FiSend,
  FiHeart,
  FiPlusSquare,
  FiUser,
  FiLogOut
} from "react-icons/fi";

const navItems = [
  { key: "home",          label: "Home",          icon: <FiHome />,       href: "/feed" },
  { key: "search",        label: "Search",        icon: <FiSearch /> },
  { key: "explore",       label: "Explore",       icon: <FiCompass />,   /*  href: "/explore" */ },
  { key: "messages",      label: "Messages",      icon: <FiSend />,       /* href: "/messages" */ },
  { key: "notifications", label: "Notifications", icon: <FiHeart /> },
  { key: "create",        label: "Create",        icon: <FiPlusSquare /> },
  { key: "profile",       label: "Profile",       icon: <FiUser /> },
];

export default function Sidebar({ onNavigate, showSearch, onSearchOpen, onSearchClose, onCreateOpen, showNotifications, onNotificationsOpen, onNotificationsClose, unreadCount }) {

  const navigate = useNavigate();

  const loggedUsername = localStorage.getItem("username");

  function handleLogout() {
    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    localStorage.removeItem("username");
    localStorage.removeItem("avatar");
    navigate("/login");
  }

  const location = useLocation();

function getActiveKey() {
  const path = location.pathname;
  console.log("current path:", path);
  if (path.startsWith("/feed")) return "home";
  if (path.startsWith("/profile")) return "profile";
  if (path.startsWith("/explore")) return "explore";
  if (path.startsWith("/messages")) return "messages";
  if (path.startsWith("/settings")) return "settings";
  return "home";
}

  const activeKey = getActiveKey();

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
            const isProfile = item.key === "profile";

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
                  <span className="ig-sidebar__icon">
                    <FiPlusSquare />
                  </span>
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

            if (isProfile) {
              return (
                <Link
                  key={item.key}
                  className={`ig-sidebar__link ${isActive ? "is-active" : ""}`}
                  to={`/profile/${loggedUsername}`}
                >
                  <span className="ig-sidebar__icon">
                    <FiUser />
                  </span>
                  <span className="ig-sidebar__label">Profile</span>
                </Link>
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
                <span className="ig-sidebar__icon" aria-hidden="true">
                  {item.icon}
                </span>
                <span className="ig-sidebar__label">{item.label}</span>
              </Link>
            );
          })}
        </nav>

        <div className="ig-sidebar__bottom">
          <button className="ig-sidebar__link" onClick={handleLogout}>
            <span className="ig-sidebar__icon"><FiLogOut /></span>
            <span className="ig-sidebar__label">Log out</span>
          </button>
        </div>

      </div>
    </aside>
  );
}