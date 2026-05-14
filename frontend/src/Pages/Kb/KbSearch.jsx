import "./Css/KbSearch.css";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

export default function KbSearch() {
  const [category, setCategory] = useState("");
  const [results, setResults] = useState([]);
  const navigate = useNavigate();

  const searchArticles = async () => {
    if (!category.trim()) {
      alert("Enter category to search");
      return;
    }

    try {
      const res = await axios.get(
        `http://localhost:8080/kb/articles/search?category=${encodeURIComponent(category)}`
      );
      setResults(res.data);
    } catch (err) {
      console.error("Error searching KB articles:", err);
    }
  };

  const openArticle = (kbId) => {
    navigate(`/kb/article/${kbId}`);
  };

  return (
    <div className="kb-search-page">
      <div className="kb-search-header">
        <h2>Search Knowledge Base</h2>
        <p>Find articles by category and review reusable solutions.</p>
      </div>

      <div className="kb-search-card">
        <div className="kb-search-toolbar">
          <input
            className="kb-search-input"
            type="text"
            placeholder="Enter category..."
            value={category}
            onChange={(e) => setCategory(e.target.value)}
          />

          <button className="kb-search-btn" onClick={searchArticles}>
            Search
          </button>
        </div>

        <div className="kb-results-section">
          {results.length === 0 ? (
            <div className="kb-empty-state">No search results yet.</div>
          ) : (
            <table className="kb-results-table">
              <thead>
                <tr>
                  <th>Title</th>
                  <th>Category</th>
                </tr>
              </thead>

              <tbody>
                {results.map((article, index) => (
                  <tr
                    key={article.kbId ?? index}
                    className="kb-clickable-row"
                    onClick={() => openArticle(article.kbId)}
                  >
                    <td className="kb-title-cell">{article.title}</td>
                    <td>
                      <span className="kb-category-pill">
                        {article.category || "General"}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </div>
  );
}
