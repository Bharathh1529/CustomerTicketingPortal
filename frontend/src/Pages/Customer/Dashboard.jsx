import "./Dashboard.css";
import { useNavigate } from "react-router-dom";
import { useState } from "react";

export default function Dashboard() {
  const navigate = useNavigate();
  const [showPopup, setShowPopup] = useState(false);
  const [ticketId, setTicketId] = useState("");

  return (
    <div className="dashboard-container">

     
      <div className="dashboard-title-block">
        <h2>Welcome!</h2>
        <p>What do you need help with today?</p>
      </div>

      {/* QUICK ACTIONS */}
      <div className="dashboard-grid">

        <div className="dashboard-card" onClick={() => navigate("/create-ticket")}>
          <div className="card-icon">📝</div>
          <h3>Report an Issue</h3>
          <p>Submit a support ticket to the IT team</p>
        </div>

        <div className="dashboard-card" onClick={() => navigate("/tickets")}>
          <div className="card-icon">📂</div>
          <h3>My Tickets</h3>
          <p>View and track your submitted tickets</p>
        </div>

        <div className="dashboard-card" onClick={() => navigate("/kb/search")}>
          <div className="card-icon">🔍</div>
          <h3>Search Knowledge Base</h3>
          <p>Find answers to common questions</p>
        </div>
        
        <div className="dashboard-card" onClick={() => setShowPopup(true)}>
        <div className="card-icon">🔖</div>
        <h3>View Ticket</h3>
        <p>Open a specific ticket</p>
        </div>

      </div>
      {showPopup && (
  <div className="popup-overlay">
    <div className="popup-container">
      <h3>Open a Ticket</h3>
      <p>Please enter the Ticket ID:</p>

      <input
        type="number"
        placeholder="Enter Ticket ID"
        value={ticketId}
        onChange={(e) => setTicketId(e.target.value)}
      />

      <div className="popup-buttons">
        <button
          className="popup-btn confirm"
          onClick={() => {
            if (ticketId.trim()) {
              navigate(`/ticket/${ticketId}`);
              setShowPopup(false);
              setTicketId("");
            }
          }}
        >
          Open
        </button>

        <button
          className="popup-btn cancel"
          onClick={() => setShowPopup(false)}
        >
          Cancel
        </button>
      </div>
    </div>
  </div>
  )}
    </div>
  );
}