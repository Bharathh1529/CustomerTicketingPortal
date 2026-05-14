import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../Auth/AuthContext";
import axios from "axios";
import "./Inbox.css";

export default function Inbox() {
  const navigate = useNavigate();
  const { user } = useAuth();

  const [tickets, setTickets] = useState([]);
  const [search, setSearch] = useState("");
  const [sortField, setSortField] = useState("");
  const [sortAsc, setSortAsc] = useState(true);

  useEffect(() => {
    if (user?.id) {
      fetchAssignedTickets(user.id);
    }
  }, [user]);

  const fetchAssignedTickets = async (agentId) => {
    try {
      const res = await axios.get(
        `http://localhost:8080/tickets/assignee/${agentId}`
      );
      setTickets(res.data);
    } catch (err) {
      console.error("Error fetching assigned tickets:", err);
    }
  };

  const handleSort = (field) => {
    if (sortField === field) {
      setSortAsc(!sortAsc);
    } else {
      setSortField(field);
      setSortAsc(true);
    }
  };

  const filteredTickets = tickets
    .filter((t) => {
      return (
        String(t.id).toLowerCase().includes(search.toLowerCase()) ||
        String(t.subject).toLowerCase().includes(search.toLowerCase()) ||
        String(t.status).toLowerCase().includes(search.toLowerCase()) ||
        String(t.priority).toLowerCase().includes(search.toLowerCase()) ||
        String(t.requesterName ?? t.requesterId ?? "")
          .toLowerCase()
          .includes(search.toLowerCase())
      );
    })
    .sort((a, b) => {
      if (!sortField) return 0;

      let aValue = a[sortField];
      let bValue = b[sortField];

      if (sortField === "requesterName") {
        aValue = a.requesterName ?? String(a.requesterId ?? "");
        bValue = b.requesterName ?? String(b.requesterId ?? "");
      }

      if (sortField === "updatedAt") {
        return sortAsc
          ? new Date(a.updatedAt) - new Date(b.updatedAt)
          : new Date(b.updatedAt) - new Date(a.updatedAt);
      }

      aValue = String(aValue ?? "");
      bValue = String(bValue ?? "");

      return sortAsc
        ? aValue.localeCompare(bValue)
        : bValue.localeCompare(aValue);
    });

  return (
    <div className="tickets-container">
      <div className="tickets-header">
        <h2>Assigned Tickets</h2>
        <p className="subtitle">Tickets currently assigned to you.</p>
      </div>

      <div className="tickets-table-card">
        <div className="table-toolbar">
          <div className="toolbar-left">
            <h4 className="toolbar-title">Search & Filter</h4>
            <p className="toolbar-subtitle">
              Search by ticket ID, subject, status, priority, or requester.
              Click a column name to sort.
            </p>
          </div>

          <div className="toolbar-right">
            <div className="search-input-wrapper">
              <span className="search-icon">🔍</span>
              <input
                type="text"
                placeholder="Search tickets..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
              />
            </div>
          </div>
        </div>

        {filteredTickets.length === 0 ? (
          <div className="no-tickets">No assigned tickets found.</div>
        ) : (
          <table className="tickets-table">
            <thead>
              <tr>
                <th
                  onClick={() => handleSort("id")}
                  className="sortable-header"
                >
                  ID
                  <span className="sort-indicator">
                    {sortField === "id" ? (sortAsc ? "▲" : "▼") : "↕"}
                  </span>
                </th>

                <th
                  onClick={() => handleSort("subject")}
                  className="sortable-header"
                >
                  Subject
                  <span className="sort-indicator">
                    {sortField === "subject" ? (sortAsc ? "▲" : "▼") : "↕"}
                  </span>
                </th>

                <th
                  onClick={() => handleSort("status")}
                  className="sortable-header"
                >
                  Status
                  <span className="sort-indicator">
                    {sortField === "status" ? (sortAsc ? "▲" : "▼") : "↕"}
                  </span>
                </th>

                <th
                  onClick={() => handleSort("priority")}
                  className="sortable-header"
                >
                  Priority
                  <span className="sort-indicator">
                    {sortField === "priority" ? (sortAsc ? "▲" : "▼") : "↕"}
                  </span>
                </th>

                <th
                  onClick={() => handleSort("requesterName")}
                  className="sortable-header"
                >
                  Requester
                  <span className="sort-indicator">
                    {sortField === "requesterName" ? (sortAsc ? "▲" : "▼") : "↕"}
                  </span>
                </th>

                <th
                  onClick={() => handleSort("updatedAt")}
                  className="sortable-header"
                >
                  Updated
                  <span className="sort-indicator">
                    {sortField === "updatedAt" ? (sortAsc ? "▲" : "▼") : "↕"}
                  </span>
                </th>
              </tr>
            </thead>

            <tbody>
              {filteredTickets.map((t) => (
                <tr
                  key={t.id}
                  className="tickets-row"
                  onClick={() => navigate(`/agent/tickets/${t.id}`)}
                >
                  <td>
                    <span
                      className="ticket-link"
                      onClick={(e) => {
                        e.stopPropagation();
                        navigate(`/agent/tickets/${t.id}`);
                      }}
                    >
                      {t.id}
                    </span>
                  </td>

                  <td className="subject-cell">{t.subject}</td>

                  <td>
                    <span
                      className={`status-pill ${String(t.status).toLowerCase()}`}
                    >
                      {t.status}
                    </span>
                  </td>

                  <td>
                    <span
                      className={`priority-pill ${String(t.priority).toLowerCase()}`}
                    >
                      {t.priority}
                    </span>
                  </td>

                  <td>{t.requesterName ?? t.requesterId ?? "Unknown"}</td>

                  <td>{t.updatedAt?.replace("T", " ").slice(0, 16)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}