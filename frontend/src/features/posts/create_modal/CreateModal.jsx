import React, { useState, useRef } from "react";
import "./createmodal.css";
import { FiX, FiChevronLeft, FiImage } from "react-icons/fi";
import { createPost } from "../../../api/postApi";

const MAX_FILES = 20;
const MAX_SIZE_MB = 50;
const MAX_SIZE_BYTES = MAX_SIZE_MB * 1024 * 1024;

export default function CreateModal({ onClose }) {

  const [step, setStep] = useState(1);
  const [files, setFiles] = useState([]);
  const [errors, setErrors] = useState([]);
  const [caption, setCaption] = useState("");
  const [dragOver, setDragOver] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const inputRef = useRef(null);

  function validateAndAddFiles(incoming) {
    const newErrors = [];
    const valid = [];

    Array.from(incoming).forEach(file => {
      if (!file.type.startsWith("image/") && !file.type.startsWith("video/")) {
        newErrors.push(`${file.name} — only images and videos are allowed.`);
        return;
      }
      if (file.size > MAX_SIZE_BYTES) {
        newErrors.push(`${file.name} — exceeds 50MB limit.`);
        return;
      }
      valid.push({
        id: Date.now() + Math.random(),
        file,
        preview: URL.createObjectURL(file),
        type: file.type.startsWith("video/") ? "video" : "image",
      });
    });

    const combined = [...files, ...valid];

    if (combined.length > MAX_FILES) {
      newErrors.push(`Maximum ${MAX_FILES} files allowed.`);
      setErrors(newErrors);
      return;
    }

    setErrors(newErrors);
    setFiles(combined);
    if (combined.length > 0) setStep(2);
  }

  function handleInputChange(e) {
    validateAndAddFiles(e.target.files);
  }

  function handleDrop(e) {
    e.preventDefault();
    setDragOver(false);
    validateAndAddFiles(e.dataTransfer.files);
  }

  function removeFile(id) {
    const updated = files.filter(f => f.id !== id);
    setFiles(updated);
    if (updated.length === 0) setStep(1);
  }

  async function handleShare() {
    setLoading(true);
    setError(null);

    try {
      await createPost(files, caption);
      onClose();
    } catch (err) {
      setError("Failed to create post. Please try again.");
      console.error("Create post failed:", err);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="ig-create-backdrop" onClick={onClose}>
      <div className="ig-create-modal" onClick={e => e.stopPropagation()}>

        <div className="ig-create-header">
          {step > 1 && (
            <button className="ig-create-back" onClick={() => setStep(step - 1)}>
              <FiChevronLeft />
            </button>
          )}
          <span>
            {step === 1 && "Create new post"}
            {step === 2 && "Preview"}
            {step === 3 && "New post"}
          </span>
          <button className="ig-create-close" onClick={onClose}>
            <FiX />
          </button>
        </div>

        <div className="ig-create-divider" />

        {/* upload */}
        {step === 1 && (
          <div
            className={`ig-create-dropzone ${dragOver ? "drag-over" : ""}`}
            onDragOver={(e) => { e.preventDefault(); setDragOver(true); }}
            onDragLeave={() => setDragOver(false)}
            onDrop={handleDrop}
            onClick={() => inputRef.current.click()}
          >
            <FiImage className="ig-create-dropzone-icon" />
            <p className="ig-create-dropzone-title">Drag photos and videos here</p>
            <p className="ig-create-dropzone-sub">Up to {MAX_FILES} files, max {MAX_SIZE_MB}MB each</p>
            <button
              className="ig-create-select-btn"
              onClick={(e) => { e.stopPropagation(); inputRef.current.click(); }}
            >
              Select from computer
            </button>
            <input
              ref={inputRef}
              type="file"
              accept="image/*,video/*"
              multiple
              hidden
              onChange={handleInputChange}
            />
          </div>
        )}

        {/* preview */}
        {step === 2 && (
          <div className="ig-create-preview">
            <div className="ig-create-preview-grid">
              {files.map((f) => (
                <div key={f.id} className="ig-create-preview-item">
                  {f.type === "video" ? (
                    <video src={f.preview} className="ig-create-preview-media" />
                  ) : (
                    <img src={f.preview} alt="" className="ig-create-preview-media" />
                  )}
                  <button className="ig-create-preview-remove" onClick={() => removeFile(f.id)}>
                    <FiX />
                  </button>
                </div>
              ))}

              {files.length < MAX_FILES && (
                <div className="ig-create-preview-add" onClick={() => inputRef.current.click()}>
                  <FiImage />
                  <span>Add more</span>
                  <input
                    ref={inputRef}
                    type="file"
                    accept="image/*,video/*"
                    multiple
                    hidden
                    onChange={handleInputChange}
                  />
                </div>
              )}
            </div>

            {errors.length > 0 && (
              <div className="ig-create-errors">
                {errors.map((err, i) => (
                  <p key={i} className="ig-create-error">{err}</p>
                ))}
              </div>
            )}

            <div className="ig-create-preview-footer">
              <span className="ig-create-count">{files.length}/{MAX_FILES} files</span>
              <button className="ig-create-next-btn" onClick={() => setStep(3)}>
                Next
              </button>
            </div>
          </div>
        )}

        {/* caption */}
        {step === 3 && (
          <div className="ig-create-caption">
            <div className="ig-create-caption-preview">
              {files[0]?.type === "video" ? (
                <video src={files[0].preview} className="ig-create-caption-thumb" />
              ) : (
                <img src={files[0]?.preview} alt="" className="ig-create-caption-thumb" />
              )}
              {files.length > 1 && (
                <span className="ig-create-caption-count">+{files.length - 1} more</span>
              )}
            </div>

            <div className="ig-create-caption-right">
              <textarea
                className="ig-create-caption-input"
                placeholder="Write a caption..."
                value={caption}
                onChange={(e) => setCaption(e.target.value)}
                maxLength={2200}
              />
              <span className="ig-create-caption-charcount">
                {caption.length}/2200
              </span>

              {error && <p className="ig-create-error">{error}</p>}

              <button
                className="ig-create-share-btn"
                onClick={handleShare}
                disabled={loading}
              >
                {loading ? "Sharing..." : "Share"}
              </button>
            </div>
          </div>
        )}

      </div>
    </div>
  );
}