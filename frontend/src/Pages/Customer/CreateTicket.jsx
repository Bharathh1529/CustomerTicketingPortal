import "./CreateTicket.css";
import { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "../../Auth/AuthContext";
import { useNavigate } from "react-router-dom";

export default function CreateTicket() {
  const { user } = useAuth();
  const navigate = useNavigate();

  const orgId = user?.orgId;
  const requesterId = user?.id;

  const [priority, setPriority] = useState("LOW");
  const [subject, setSubject] = useState("");
  const [body, setBody] = useState("");

  const [agents, setAgents] = useState([]);
  const [assigneeId, setAssigneeId] = useState("");

  useEffect(() => {
    if (user?.id) {
      fetchAgents();
    }
  }, [user]);
  const fetchAgents = async () => {
    try {
      const res = await axios.get("http://localhost:8080/tickets/agents");
      setAgents(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const submitTicket = async () => {
    if (!orgId) {
      alert("You are not logged in - OrgId missing");
      return;
    }

    if (!requesterId) {
      alert("Requester ID missing");
      return;
    }

    if (!subject || !body) {
      alert("Subject and Description required");
      return;
    }

    try {
      await axios.post("http://localhost:8080/tickets", {
        orgId: orgId,
        requesterId: requesterId,
        subject: subject,
        body: body,
        priority: priority,

        assigneeId: assigneeId === "" ? null : Number(assigneeId),
      });

      alert("Ticket created successfully!");
      navigate("/");
    } catch (err) {
      console.error(err);
      alert("Error creating ticket");
    }
  };

  return (
    <div className="report-container">
      <div className="report-title-section">
        <h2>Report an Issue</h2>
        <p>
          Use this form to request assistance with the issue you are
          experiencing.
        </p>
        <p>
          On submitting this form an incident ticket will be created and
          assigned to IT Support.
        </p>
      </div>

      <div className="report-layout">
        <div className="report-form-card">
          <label>Subject *</label>
          <input
            type="text"
            placeholder="Short title for your issue"
            value={subject}
            onChange={(e) => setSubject(e.target.value)}
          />

          <label>Description *</label>
          <textarea
            placeholder="Describe your issue..."
            value={body}
            onChange={(e) => setBody(e.target.value)}
          />

          <label>Priority *</label>
          <select
            value={priority}
            onChange={(e) => setPriority(e.target.value)}
          >
            <option value="LOW">Low</option>
            <option value="MEDIUM">Medium</option>
            <option value="HIGH">High</option>
            <option value="URGENT">Urgent</option>
          </select>

          <label>Assign To</label>
          <select
            className="assign-dropdown"
            value={assigneeId}
            onChange={(e) => setAssigneeId(e.target.value)}
          >
            <option value="">Unassigned</option>

            {agents.map((agent) => (
              <option key={agent.id} value={agent.id}>
                {agent.name}
              </option>
            ))}
          </select>
        </div>

        <div className="submit-panel">
          <button className="submit-btn" onClick={submitTicket}>
            Submit
          </button>

          <div className="info-box">
            <h4>Important Information</h4>
            <p>Include as many details as possible for faster resolution.</p>
          </div>
        </div>
      </div>
    </div>
  );
}
