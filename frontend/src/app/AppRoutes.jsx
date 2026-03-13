import { Routes, Route, Navigate } from "react-router-dom";
import Layout from "../components/layout/Layout";
import Feed from "../features/posts/Feed";
import ProfileTest from "../features/profile/ProfileTest";
import Login from "../features/auth/Login";
import Register from "../features/auth/Register";
import ProfilePage from "../features/profile/ProfilePage";
import Settings from "../features/settings/Settings";

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<Login/>}></Route>
      <Route path="/register" element={<Register/>}></Route>

      <Route path="/" element={<Navigate to="/feed" replace />} />

      
      <Route element={<Layout />}>
        <Route path="/feed" element={<Feed />} />
      </Route>

      <Route element={<Layout />}>
        <Route path="/profile/:username" element={<ProfilePage />} />
      </Route>

      <Route element={<Layout/>}>
        <Route path="/settings" element={<Settings/>} />
      </Route>



      
      <Route path="/profiletest" element={<ProfileTest/>} ></Route>
      
    </Routes>
  );
}
