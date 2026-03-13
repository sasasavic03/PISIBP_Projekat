import { Outlet } from "react-router-dom";
import { useState } from "react";
import Sidebar from "./sidebar/Sidebar";
import SearchPanel from "../../features/search/SearchPanel";
import CreateModal from "../../features/posts/create_modal/CreateModal";
import NotificationsPanel from "../../features/notifications/NotificationsPanel";
import "./layout.css";
import { notificationsMock } from "../../data/notifications.mock";

export default function Layout() {

  const [showSearch, setShowSearch] = useState(false);
  const [showCreate, setShowCreate] = useState(false);
  const [showNotifications, setShowNotifications] = useState(false);

  const [notifications, setNotifications] = useState(notificationsMock);
  const unreadCount = notifications.filter(n => !n.read).length;

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

