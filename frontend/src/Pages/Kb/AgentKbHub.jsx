import "./Css/AgentKbHub.css";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import KbList from "./KbList";
import KbSearch from "./KbSearch";

export default function AgentKbHub() {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState("list");

  return (
    <div className="agent-kb-page">
      <div className="agent-kb-header">
        <div>
          <h2>Knowledge Base</h2>
          <p>Search, review, and create support articles.</p>
        </div>

        <button
          className="agent-kb-create-btn"
          onClick={() =>
            navigate("/kb/create", { state: { from: "/agent/kb" } })
          }
        >
          Create Article
        </button>
      </div>

      <div className="agent-kb-tabs">
        <button
          className={`agent-kb-tab ${activeTab === "list" ? "active" : ""}`}
          onClick={() => setActiveTab("list")}
        >
          All Articles
        </button>

        <button
          className={`agent-kb-tab ${activeTab === "search" ? "active" : ""}`}
          onClick={() => setActiveTab("search")}
        >
          Search Articles
        </button>
      </div>

      <div className="agent-kb-content">
        {activeTab === "list" ? <KbList /> : <KbSearch />}
      </div>
    </div>
  );
}