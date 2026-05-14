import "./TicketView.css";
import axios from "axios";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useAuth } from "../../Auth/AuthContext";

export default function TicketView() {
  const { id } = useParams();
  const { user } = useAuth();

  const [ticket, setTicket] = useState(null);
  const [messages, setMessages] = useState([]);
  const [reply, setReply] = useState("");
  const [customerCanReply, setCustomerCanReply] = useState(true);
  const [errorMsg, setErrorMsg] = useState("");

  useEffect(() => {
    fetchTicket();
  }, [id]);

  const fetchTicket = async () => {
    try {
      const res = await axios.get(`http://localhost:8080/tickets/${id}`);
      setTicket(res.data);
      setMessages(res.data.message || []);
      setErrorMsg("");

      const lastAgentMsg = [...(res.data.message || [])]
        .reverse()
        .find((m) => m.from === "AGENT" && m.internal === false);

      if (res.data.status === "CLOSED") {
        setCustomerCanReply(false);
      } else if (lastAgentMsg && lastAgentMsg.waitOnCustomer === false) {
        setCustomerCanReply(false);
      } else {
        setCustomerCanReply(true);
      }
    } catch (err) {
      console.error(err);
      setErrorMsg("Unable to load ticket.");
    }
  };

  const sendReply = async () => {
    if (ticket?.status === "CLOSED") {
      const msg = "This ticket is closed. You cannot send messages anymore.";
      setErrorMsg(msg);
      alert(msg);
      return;
    }

    try {
      await axios.post(`http://localhost:8080/tickets/${id}/messages`, {
        authorUserId: user.id,
        body: reply,
        internal: false,
        waitOnCustomer: true
      });

      setReply("");
      setErrorMsg("");
      fetchTicket();
    } catch (err) {
      console.error(err);

      const backendMessage =
        err?.response?.data?.message ||
        err?.response?.data?.error ||
        err?.response?.data ||
        "Unable to send message.";

      const finalMessage =
        typeof backendMessage === "string"
          ? backendMessage
          : "Unable to send message.";

      setErrorMsg(finalMessage);
      alert(finalMessage);
    }
  };

  if (!ticket) return <div>Loading...</div>;

  const isClosed = ticket.status === "CLOSED";

  return (
    <div className="ticketview-container">
      <div className="ticketview-layout">
        <div className="chat-section">
          <h2 className="ticket-title">{ticket.subject}</h2>
          <p className="ticket-id">Ticket #{ticket.id}</p>

          <div className="chat-box">
            {messages.map((msg) => (
              <div
                key={msg.id}
                className={`chat-message ${msg.from === "AGENT" ? "agent" : "customer"}`}
              >
                <span className="msg-author">{msg.from}</span>
                <p className="msg-body">{msg.body}</p>
                <span className="msg-time">
                  {msg.createdAt.replace("T", " ").slice(0, 16)}
                </span>
              </div>
            ))}
          </div>

          {errorMsg && <div className="reply-error">{errorMsg}</div>}

          {isClosed ? (
            <div className="reply-disabled">
              This ticket is closed. You cannot send messages anymore.
            </div>
          ) : customerCanReply ? (
            <div className="reply-box">
              <textarea
                placeholder="Type your message..."
                value={reply}
                onChange={(e) => setReply(e.target.value)}
              ></textarea>

              <button className="reply-btn" onClick={sendReply}>
                Send Reply
              </button>
            </div>
          ) : (
            <div className="reply-disabled">
              You cannot reply because the agent disabled customer input.
            </div>
          )}
        </div>

        <div className="info-panel">
          <h3>Ticket Details</h3>

          <div className="info-item">
            <span>Status:</span>
            <strong>{ticket.status}</strong>
          </div>

          <div className="info-item">
            <span>Priority:</span>
            <strong>{ticket.priority}</strong>
          </div>

          <div className="info-item">
            <span>Assignee:</span>
            <strong>{ticket.assigneeId ?? "Unassigned"}</strong>
          </div>

          <div className="info-item">
            <span>Created:</span>
            <strong>{ticket.createdAt.replace("T", " ").slice(0, 16)}</strong>
          </div>

          <div className="info-item">
            <span>Updated:</span>
            <strong>{ticket.updatedAt.replace("T", " ").slice(0, 16)}</strong>
          </div>

          <div className="info-item">
            <span>SLA - First Response:</span>
            <strong>{ticket.firstResponseDueAt ?? "N/A"}</strong>
          </div>
          <div className="info-item">
            <span>SLA - Resolution:</span>
            <strong>{ticket.resolutionDueAt ?? "N/A"}</strong>
          </div>
        </div>
      </div>
    </div>
  );
}