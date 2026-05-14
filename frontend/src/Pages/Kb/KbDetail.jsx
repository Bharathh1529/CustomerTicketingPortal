import "./Css/KbDetail.css";
import axios from "axios";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

export default function KbDetail() {
  const { id } = useParams();
  const [article, setArticle] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchArticle();
  }, [id]);

  const fetchArticle = async () => {
    try {
      const res = await axios.get(`http://localhost:8080/kb/articles/${id}`);
      setArticle(res.data);
      setError("");
    } catch (err) {
      console.error("Error loading article:", err);
      setError("Unable to load article.");
    }
  };

  if (error) return <div>{error}</div>;
  if (!article) return <div>Loading...</div>;

  return (
    <div className="kb-detail-page">
      <div className="kb-detail-header">
        <h2>{article.title}</h2>
        <p>{article.category || "General"}</p>
      </div>

      <div className="kb-detail-card">
        <h3>Issue & Solution</h3>
        <div className="kb-detail-body">
          {article.body}
        </div>

        <div className="kb-detail-meta">
          <span className="kb-detail-meta-label">Provided By</span>
          <span className="kb-detail-meta-value">
            {article.createdByName || "Support Agent"}
          </span>
        </div>
      </div>
    </div>
  );
}