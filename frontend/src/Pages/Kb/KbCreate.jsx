import "./Css/KbCreate.css";
import axios from "axios";
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

export default function KbCreate() {
  const location = useLocation();
  const navigate = useNavigate();

  const [title, setTitle] = useState("");
  const [category, setCategory] = useState("");
  const [body, setBody] = useState("");

  useEffect(() => {
    if (location.state) {
      setTitle(location.state.title || "");
      setCategory(location.state.category || "");
      setBody(location.state.body || "");
    }
  }, [location.state]);

  const createArticle = async () => {
    if (!title || !body) {
      alert("Title and body are required.");
      return;
    }

    try {
      await axios.post("http://localhost:8080/kb/articles", {
        title,
        category,
        body
      });

      alert("KB Article created successfully.");
      navigate("/kb");
    } catch (err) {
      console.error(err);
      alert("Failed to create KB article.");
    }
  };

  return (
    <div className="kb-page">
      <div className="kb-header">
        <h2>Create Knowledge Base Article</h2>
        <p>Create a reusable solution from a resolved issue.</p>
      </div>

      <div className="kb-card">
        <div className="kb-form-group">
          <label>Title</label>
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="Enter article title"
          />
        </div>

        <div className="kb-form-group">
          <label>Category</label>
          <input
            type="text"
            value={category}
            onChange={(e) => setCategory(e.target.value)}
            placeholder="Enter category"
          />
        </div>

        <div className="kb-form-group">
          <label>Body</label>
          <textarea
            value={body}
            onChange={(e) => setBody(e.target.value)}
            placeholder="Enter article content"
          />
        </div>

        <div className="kb-actions">
          <button className="kb-cancel-btn" onClick={() => navigate(-1)}>
            Cancel
          </button>
          <button className="kb-save-btn" onClick={createArticle}>
            Save Article
          </button>
        </div>
      </div>
    </div>
  );
}