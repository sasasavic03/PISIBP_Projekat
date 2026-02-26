import { Outlet } from "react-router-dom";
import Sidebar from "./sidebar/Sidebar";
import "./layout.css";

export default function Layout() {
  return (
    <div className="layout">
      <aside className="layout-sidebar">
        <Sidebar />
      </aside>
      

      <main className="layout-main">
        <Outlet />
      </main>
    </div>

  );
}

