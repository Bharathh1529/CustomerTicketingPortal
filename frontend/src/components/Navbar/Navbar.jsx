import { useAuth } from "../../Auth/AuthContext";
import { useNavigate } from "react-router-dom";
import "./Navbar.css";
import { useState, useEffect, useRef } from "react";

export default function Navbar() {
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const [menuOpen, setMenuOpen] = useState(false);

  const dropdownRef = useRef(null);

  // ✅ Close dropdown when clicking outside
  useEffect(() => {
    function handleClickOutside(e) {
      if (dropdownRef.current && !dropdownRef.current.contains(e.target)) {
        setMenuOpen(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);

    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <>
      {/* ✅ Background blur when menu open */}
      {menuOpen && <div className="blur-overlay"></div>}

      <header className="navbar">
        {/* LEFT */}
        <div
          className="navbar-left"
          onClick={() => {
            if (!user) {
              navigate("/login");
            } else if (user.role === "AGENT" || user.role === "SUPERVISOR") {
              navigate("/agent/dashboard");
            } else {
              navigate("/");
            }
          }}
        >
          <div className="navbar-logo">?</div>
          <span className="navbar-title">Help Portal</span>
        </div>

        {/* RIGHT */}
        <div className="navbar-right">
          {!user ? (
            <span className="nav-link" onClick={() => navigate("/login")}>
              Login
            </span>
          ) : (
            <>
              <span className="nav-link" onClick={() => navigate("/tickets")}>
                My Items
              </span>

              <span className="nav-link">Service Disruption</span>

              {/* ✅ USER SECTION */}
              <div
                className="user-section"
                ref={dropdownRef}
                onClick={() => setMenuOpen((prev) => !prev)}
              >
                <div className="user-avatar">
                  {user.name ? user.name[0].toUpperCase() : "U"}
                </div>
                <span className="user-name">{user.name.split(" ")[0]}</span>

                {menuOpen && (
                  <div className="dropdown-menu">
                    <div onClick={() => navigate("/profile")}>Profile</div>
                    <div onClick={logout}>Logout</div>
                  </div>
                )}
              </div>
            </>
          )}
        </div>
      </header>
    </>
  );
}
