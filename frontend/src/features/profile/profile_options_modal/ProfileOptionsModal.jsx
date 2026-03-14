import React from "react";
import "./profileoptionsmodal.css";
import { blockUser } from "../../api/userApi";
import { useNavigate } from "react-router-dom";

export default function ProfileOptionsModal({ userId, onClose }) {

  const navigate = useNavigate();

  async function handleBlock() {
    try {
      await blockUser(userId);
      onClose();
      navigate(-1); 
    } catch (err) {
      console.error("Failed to block user:", err);
    }
  }

  async function handleReport() {
    
    onClose();
  }

  return (
    <div className="ig-profileoptions-backdrop" onClick={onClose}>
      <div className="ig-profileoptions-panel" onClick={(e) => e.stopPropagation()}>

        <button
          className="ig-profileoptions-item ig-profileoptions-danger"
          onClick={handleBlock}
        >
          Block
        </button>
        <div className="ig-profileoptions-divider" />

        <button
          className="ig-profileoptions-item ig-profileoptions-danger"
          onClick={handleReport}
        >
          Report
        </button>
        <div className="ig-profileoptions-divider" />

        <button className="ig-profileoptions-item">
          Send message
        </button>
        <div className="ig-profileoptions-divider" />

        <button className="ig-profileoptions-item" onClick={onClose}>
          Cancel
        </button>

      </div>
    </div>
  );
}