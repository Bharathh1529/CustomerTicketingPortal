import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../Auth/AuthContext";
import "./Login.css";

export default function Login() {
  const navigate = useNavigate();
  const { login, user } = useAuth();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  useEffect(() => {
    if (user) {
      if (user.role === "AGENT" || user.role === "SUPERVISOR") {
        navigate("/agent/dashboard", { replace: true });
      } else {
        navigate("/", { replace: true });
      }
    }
  }, [user, navigate]);

  const submitLogin = async () => {
    try {
      const res = await axios.post("http://localhost:8080/auth/login", {
        email,
        password,
      });

      login({
        id: res.data.id,
        orgId: res.data.orgId,
        name: res.data.name,
        role: res.data.role,
        token: res.data.token,
      });

      axios.defaults.headers.common["Authorization"] =
        "Bearer " + res.data.token;

      if (res.data.role === "AGENT" || res.data.role === "SUPERVISOR") {
        navigate("/agent/dashboard", { replace: true });
      } else {
        navigate("/", { replace: true });
      }
    } catch (err) {
      alert("Invalid Login!");
    }
  };

  return (
    <div className="login-page">
      <div className="login-card">
        <div className="login-content">
          <h2>Welcome Back</h2>
          <p className="login-description">
            Sign in to track tickets, raise issues, and connect with support.
          </p>

          <label className="login-label">Email</label>
          <input
            type="text"
            placeholder="Enter your email"
            onChange={(e) => setEmail(e.target.value)}
          />

          <label className="login-label">Password</label>
          <input
            type="password"
            placeholder="Enter your password"
            onChange={(e) => setPassword(e.target.value)}
          />

          <button onClick={submitLogin}>Login</button>

          <p onClick={() => navigate("/signup")} className="login-link">
            Don&apos;t have an account? <span>Sign up</span>
          </p>
        </div>
      </div>
    </div>
  );
}