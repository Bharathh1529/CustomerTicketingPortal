import { useEffect, useState } from "react";
import axios from "axios";
import "./Css/KbList.css";
export default function KbList() {
  const [articles, setArticles] = useState([]);

  useEffect(() => {
    fetchArticles();
  }, []);

  const fetchArticles = async () => {
    try {
      const res = await axios.get("http://localhost:8080/kb/articles/searchAll");
      setArticles(res.data);
    } catch (err) {
      console.error("Error fetching KB articles:", err);
    }
  };

  return (
    <div>
      {articles.length === 0 ? (
        <div className="kb-empty">No KB articles found.</div>
      ) : (
        <table className="kb-table">
          <thead>
            <tr>
              <th>Title</th>
              <th>Category</th>
              <th>Body Preview</th>
            </tr>
          </thead>

          <tbody>
            {articles.map((article) => (
              <tr key={article.id}>
                <td>{article.title}</td>
                <td>{article.category || "General"}</td>
                <td className="kb-body-preview">
                  {(article.body || "").slice(0, 100)}
                  {(article.body || "").length > 100 ? "..." : ""}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
