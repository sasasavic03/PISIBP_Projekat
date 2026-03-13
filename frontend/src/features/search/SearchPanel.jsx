import React, { useState } from "react";
import { Link } from "react-router-dom";
import "./searchpanel.css";
import { FiSearch, FiX } from "react-icons/fi";
import { profilesMock } from "../../data/profile.mock";

export default function SearchPanel({ onClose }) {
  const [query, setQuery] = useState("");
/* radi sa mock podacima */
  const results = query.trim() === ""
    ? []
    : profilesMock.filter(p =>
        p.user.username.toLowerCase().includes(query.toLowerCase()) ||
        p.user.fullName.toLowerCase().includes(query.toLowerCase())
      );

/* sa backom */
/* const [results, setResults] = useState([]);

useEffect(() => {
  if (query.trim() === "") {
    setResults([]);
    return;
  }

  fetch(`http://localhost:8080/api/users/search?q=${query}`)
    .then(res => res.json())
    .then(data => setResults(data));

}, [query]); */

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
        ) : results.length === 0 ? (
          <p className="ig-searchpanel-hint">No results for "{query}".</p>
        ) : (
          results.map((p) => (
            <Link
              key={p.user.id}
              to={`/profile/${p.user.username}`}
              className="ig-searchpanel-item"
              onClick={onClose}
            >
              <img src={p.user.avatar} alt={p.user.username} />
              <div className="ig-searchpanel-item-info">
                <span className="ig-searchpanel-username">{p.user.username}</span>
                <span className="ig-searchpanel-fullname">{p.user.fullName}</span>
              </div>
            </Link>
          ))
        )}
      </div>

    </div>
  );
}