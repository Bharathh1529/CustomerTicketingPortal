import "../Agent/AgentTicketView.css";
import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useAuth } from "../../Auth/AuthContext";

export default function AgentTicketView() {
  const { id } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();
  const [ticket, setTicket] = useState(null);
  const [messages, setMessages] = useState([]);
  const [reply, setReply] = useState("");
  const [internal, setInternal] = useState(false);
  const [waitOnCustomer, setWaitOnCustomer] = useState(true);
  const [status, setStatus] = useState("");
  const [priority, setPriority] = useState("");
  useEffect(() => {
    loadTicket();
  }, [id]);

  const loadTicket = async () => {
    try {
      const res = await axios.get(`http://localhost:8080/tickets/${id}`);
      setTicket(res.data);
      setStatus(res.data.status);
      setPriority(res.data.priority);
      setMessages(res.data.message);
    } catch (err) {
      console.error(err);
    }
  };

  const sendReply = async () => {
    try {
      await axios.post(`http://localhost:8080/tickets/${id}/messages`, {
        authorUserId: user.id,
        body: reply,
        internal: internal,
        waitOnCustomer: waitOnCustomer,
      });
      setReply("");
      loadTicket();
    } catch (err) {
      console.error(err);
    }
  };
  const updateTicket = async () => {
    try {
      await axios.patch(`http://localhost:8080/tickets/${id}`, {
        status: status,
        priority: priority,
      });

      alert("Ticket Updated");
      loadTicket();
    } catch (err) {
      console.error(err);
    }
  };
  const closeTicket = async () => {
    try {
      await axios.patch(
        `http://localhost:8080/tickets/${id}/close?agentId=${user.id}`,
      );

      alert("Ticket Closed");

      navigate("/kb/create", {
        state: {
          title: ticket.subject,
          category: "",
          body: `Issue: ${ticket.subject}\n\nResolution:\n`,
        },
      });
    } catch (err) {
      console.error(err);
    }
  };

  if (!ticket) return <div>Loading...</div>;

  return (
    <div className="ticketview-container">
      <div className="ticketview-layout">
        {/* LEFT CHAT */}
        <div className="chat-section">
          <h2 className="ticket-title">{ticket.subject}</h2>
          <p className="ticket-id">Ticket #{ticket.id}</p>

          <div className="chat-box">
            {messages.map((msg) => (
              <div
                key={msg.id}
                className={`chat-message ${
                  msg.from === "AGENT" ? "agent" : "customer"
                }`}
              >
                <span className="msg-author">{msg.from}</span>
                <p className="msg-body">{msg.body}</p>
                <span className="msg-time">
                  {msg.createdAt.replace("T", " ").slice(0, 16)}
                </span>
              </div>
            ))}
          </div>

          {/* REPLY BOX */}
          <div className="reply-box">
            <textarea
              placeholder="Type your message..."
              value={reply}
              onChange={(e) => setReply(e.target.value)}
            />

            <label className="checkbox-row">
              <input
                type="checkbox"
                checked={internal}
                onChange={(e) => setInternal(e.target.checked)}
              />
              Internal note (not visible to customer)
            </label>

            <label className="checkbox-row">
              <input
                type="checkbox"
                checked={waitOnCustomer}
                onChange={(e) => setWaitOnCustomer(e.target.checked)}
              />
              Wait on customer (allow reply)
            </label>

            <button className="reply-btn" onClick={sendReply}>
              Send Reply
            </button>
          </div>
        </div>
        {/* RIGHT INFO PANEL */}
        <div className="info-panel">
          <h3>Ticket Details</h3>

          <div className="info-item">
            <span>Status:</span>

            <select
              className="edit-select"
              value={status}
              onChange={(e) => setStatus(e.target.value)}
            >
              <option value="NEW">NEW</option>
              <option value="OPEN">OPEN</option>
              <option value="ASSIGNED">ASSIGNED</option>
              <option value="RESOLVED">RESOLVED</option>
              <option value="CLOSED">CLOSED</option>
            </select>
          </div>

          <div className="info-item">
            <span>Priority:</span>

            <select
              className="edit-select"
              value={priority}
              onChange={(e) => setPriority(e.target.value)}
            >
              <option value="LOW">LOW</option>
              <option value="MEDIUM">MEDIUM</option>
              <option value="HIGH">HIGH</option>
              <option value="URGENT">URGENT</option>
            </select>
          </div>

          <div className="info-item">
            <span>Requester ID:</span>
            <strong>{ticket.requesterId}</strong>
          </div>

          <div className="info-item">
            <span>Created:</span>
            <strong>{ticket.createdAt.replace("T", " ").slice(0, 16)}</strong>
          </div>

          <div className="info-item">
            <span>Updated:</span>
            <strong>{ticket.updatedAt.replace("T", " ").slice(0, 16)}</strong>
          </div>

          <button className="update-btn" onClick={updateTicket}>
            Update Ticket
          </button>

          <button className="close-btn" onClick={closeTicket}>
            Close Ticket
          </button>
        </div>{" "}
      </div>
    </div>
  );
}
