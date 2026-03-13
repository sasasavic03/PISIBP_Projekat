import React from "react";
import "./profileoptionsmodal.css";

export default function ProfileOptionsModal({ onClose }) {
  return (
    <div className="ig-profileoptions-backdrop" onClick={onClose}>
      <div className="ig-profileoptions-panel" onClick={(e) => e.stopPropagation()}>

        <button className="ig-profileoptions-item ig-profileoptions-danger">
          Block
        </button>
        <div className="ig-profileoptions-divider" />

        <button className="ig-profileoptions-item ig-profileoptions-danger">
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