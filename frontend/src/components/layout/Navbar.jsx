import { Link, useNavigate } from "react-router-dom";

export default function Navbar() {
  const navigate = useNavigate();

  const handleLogout = () => {
    // kasniije clear token, auth context
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <nav className="navbar">
      <div className="navbar-left">
        <Link to="/feed" className="logo">
          Instagram
        </Link>
      </div>

      <div className="navbar-center">
        <input
          type="text"
          placeholder="Search"
          className="search-input"
        />
      </div>

      <div className="navbar-right">
        <Link to="/profile">Profile</Link>
        <button onClick={handleLogout} className="logout-btn">
          Logout
        </button>
      </div>
    </nav>
  );
}
