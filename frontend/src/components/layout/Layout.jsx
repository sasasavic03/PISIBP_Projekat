import { Outlet } from "react-router-dom";
import { useState, useEffect } from "react";
import Sidebar from "./sidebar/Sidebar";
import SearchPanel from "../../features/search/SearchPanel";
import CreateModal from "../../features/posts/create_modal/CreateModal";
import NotificationsPanel from "../../features/notifications/NotificationsPanel";
import "./layout.css";
import { getNotifications } from "../../api/notificationsApi";

export default function Layout() {

  const [showSearch, setShowSearch] = useState(false);
  const [showCreate, setShowCreate] = useState(false);
  const [showNotifications, setShowNotifications] = useState(false);
  const [notifications, setNotifications] = useState([]);

  const userId = localStorage.getItem("userId");
  const unreadCount = notifications.filter(n => !n.read).length;

  useEffect(() => {
    async function fetchNotifications() {
      try {
        const data = await getNotifications(userId);
        setNotifications(data);
      } catch (err) {
        console.error("Failed to fetch notifications:", err);
      }
    }

    if (userId) fetchNotifications();
  }, [userId]);

  return (
    <div className="layout">
      <aside className="layout-sidebar">
        <Sidebar
          onSearchOpen={() => setShowSearch(true)}
          onSearchClose={() => setShowSearch(false)}
          showSearch={showSearch}
          onCreateOpen={() => setShowCreate(true)}
          showNotifications={showNotifications}
          onNotificationsOpen={() => setShowNotifications(true)}
          onNotificationsClose={() => setShowNotifications(false)}
          unreadCount={unreadCount}
        />
      </aside>

      {showSearch && (
        <SearchPanel onClose={() => setShowSearch(false)} />
      )}

      {showCreate && (
        <CreateModal onClose={() => setShowCreate(false)} />
      )}

      {showNotifications && (
        <NotificationsPanel
          notifications={notifications}
          setNotifications={setNotifications}
          onClose={() => setShowNotifications(false)}
        />
      )}

      <main className="layout-main">
        <Outlet />
      </main>
    </div>
  );
}