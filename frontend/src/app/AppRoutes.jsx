import { Routes, Route, Navigate } from "react-router-dom";
import Layout from "../components/layout/Layout";
import Feed from "../features/posts/Feed";
import ProfileTest from "../features/profile/ProfileTest";

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/feed" replace />} />
      
      <Route element={<Layout />}>
        <Route path="/feed" element={<Feed />} />
      </Route>

      
      <Route path="/profiletest" element={<ProfileTest/>} ></Route>
      
    </Routes>
  );
}
