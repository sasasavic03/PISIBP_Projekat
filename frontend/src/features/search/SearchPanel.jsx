import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import "./searchpanel.css";
import { FiSearch, FiX } from "react-icons/fi";
import { searchUsers } from "../../api/searchApi";

export default function SearchPanel({ onClose }) {
  const [query, setQuery] = useState("");
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (query.trim() === "") {
      setResults([]);
      return;
    }

    const timeout = setTimeout(async () => {
      setLoading(true);
      try {
        const data = await searchUsers(query);
        setResults(data);
      } catch (err) {
        console.error("Search failed:", err);
      } finally {
        setLoading(false);
      }
    }, 400); 

    return () => clearTimeout(timeout);
  }, [query]);

  return (
    <div className="ig-searchpanel">

      <div className="ig-searchpanel-header">
        <h2>Search</h2>
      </div>

      <div className="ig-searchpanel-input-wrap">
        <FiSearch className="ig-searchpanel-icon" />
        <input
          type="text"
          placeholder="Search"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          autoFocus
        />
        {query && (
          <button className="ig-searchpanel-clear" onClick={() => setQuery("")}>
            <FiX />
          </button>
        )}
      </div>

      <div className="ig-searchpanel-divider" />

      <div className="ig-searchpanel-results">
        {query.trim() === "" ? (
          <p className="ig-searchpanel-hint">Try searching for people.</p>
        ) : loading ? (
          <p className="ig-searchpanel-hint">Searching...</p>
        ) : results.length === 0 ? (
          <p className="ig-searchpanel-hint">No results for "{query}".</p>
        ) : (
          results.map((user) => (
            <Link
              key={user.id}
              to={`/profile/${user.username}`}
              className="ig-searchpanel-item"
              onClick={onClose}
            >
              <img src={user.profilePictureUrl || "/default-avatar.svg"} alt={user.username} />
              <div className="ig-searchpanel-item-info">
                <span className="ig-searchpanel-username">{user.username}</span>
                <span className="ig-searchpanel-fullname">{user.fullName}</span>
              </div>
            </Link>
          ))
        )}
      </div>

    </div>
  );
}