import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../Auth/AuthContext";
import axios from "axios";
import "./AgentDashboard.css";

export default function AgentDashboard() {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [stats, setStats] = useState({});

  useEffect(() => {
    if (user?.id) {
      fetchStats(user.id);
    }
  }, [user]);

  const fetchStats = async (agentId) => {
    try {
      const res = await axios.get(
        `http://localhost:8080/tickets/agent/dashboard/${agentId}`,
      );
      setStats(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="dashboard-container">
      <div className="dashboard-title-block">
        <h2>Hello, {user?.name} 👋</h2>
        <p>Welcome to your Agent Console</p>
      </div>

      <div className="dashboard-stats">
        <div className="stat-card assigned-card">
          <div className="stat-top-line"></div>
          <span className="stat-label">Assigned</span>
          <h3>{stats.assigned ?? 0}</h3>
          <p className="stat-subtext">Tickets currently owned by you</p>
        </div>

        <div className="stat-card unassigned-card">
          <div className="stat-top-line"></div>
          <span className="stat-label">Unassigned</span>
          <h3>{stats.unassigned ?? 0}</h3>
          <p className="stat-subtext">Tickets waiting to be picked up</p>
        </div>

        <div className="stat-card pending-card">
          <div className="stat-top-line"></div>
          <span className="stat-label">Pending</span>
          <h3>{stats.pending ?? 0}</h3>
          <p className="stat-subtext">Waiting for next action</p>
        </div>

        <div className="stat-card closed-card">
          <div className="stat-top-line"></div>
          <span className="stat-label">Closed Today</span>
          <h3>{stats.closedToday ?? 0}</h3>
          <p className="stat-subtext">Resolved within the last 24 hours</p>
        </div>
      </div>

      <div className="dashboard-grid">
        <div
          className="dashboard-card"
          onClick={() => navigate("/agent/inbox")}
        >
          <div className="card-icon card-icon-blue">📥</div>
          <h3>Inbox</h3>
          <p>Assigned tickets</p>
        </div>

        <div
          className="dashboard-card"
          onClick={() => navigate("/agent/unassigned")}
        >
          <div className="card-icon card-icon-orange">📭</div>
          <h3>Unassigned Tickets</h3>
          <p>Pick new tickets</p>
        </div>

        <div className="dashboard-card" onClick={() => navigate("/agent/kb")}>
          <div className="card-icon card-icon-purple">📝</div>
          <h3>Knowledge Base</h3>
          <p>Create & manage articles</p>
        </div>
      </div>
    </div>
  );
}
