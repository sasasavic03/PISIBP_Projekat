import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import "./suggestions.css";
import { getSuggestions } from "../../../api/userApi";

export default function Suggestions() {

  const [users, setUsers] = useState([]);
  const [followed, setFollowed] = useState({});

  useEffect(() => {
    async function fetchSuggestions() {
      try {
        const data = await getSuggestions();
        setUsers(data);
      } catch (err) {
        console.error("Failed to fetch suggestions:", err);
      }
    }
    fetchSuggestions();
  }, []);

  function handleFollow(userId) {
    setFollowed(prev => ({ ...prev, [userId]: !prev[userId] }));
    
  }

  return (
    <aside className="ig-suggestions">
      <div className="ig-suggestions_inner">
        <div className="ig-suggestions_header">
          <p>Suggestions for you</p>
          <button className="ig-suggestions_seeall">See all</button>
        </div>

        <ol className="ig-suggestions_list">
          {users.map(user => (
            <li key={user.id} className="ig-suggestions_item">
              <Link to={`/profile/${user.username}`} className="ig-suggestions_link">
                <img
                  src={user.profilePictureUrl || "/default-avatar.svg"}
                  alt={user.username}
                  className="ig-suggestions_avatar"
                />
                <div className="ig-suggestions_text">
                  <span className="ig-suggestions_username">{user.username}</span>
                  <span className="ig-suggestions_followed">
                    Followed by {user.followedBy}
                  </span>
                </div>
              </Link>

              <button
                className="ig-suggestions_follow"
                onClick={() => handleFollow(user.id)}
              >
                {followed[user.id] ? "Following" : "Follow"}
              </button>
            </li>
          ))}
        </ol>

        <div>
          <span className="ig-suggestions_more">
            <Link>About</Link> &middot;
            <Link>Home</Link> &middot;
            <Link>Press</Link> &middot;
            <Link>API</Link> &middot;
            <Link>Jobs</Link> &middot;
            <Link>Privacy</Link> &middot;
            <Link>Terms</Link> &middot;
            <Link>Locations</Link> &middot;
            <Link>Languages</Link>
          </span>
        </div>

        <div>
          <span className="ig-suggestions_more">
            <p>&copy; 2026 Instagram from ERDOGLIJA KINGDOM</p>
          </span>
        </div>

      </div>
    </aside>
  );
}
