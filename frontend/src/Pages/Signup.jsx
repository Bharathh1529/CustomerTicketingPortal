import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import "./Signup.css";

export default function Signup() {
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const register = async () => {
    try {
      await axios.post("http://localhost:8080/setup/user", null, {
        params: {
          name,
          email,
          password,
          orgId: 1,
          role: "USER",
        },
      });

      alert("Account Created!");
      navigate("/login");
    } catch (err) {
      alert("Signup Failed");
      console.error(err);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card">
        
        {/* Form Section */}
        <div className="auth-content auth-content-center">
          <h2>Create Account</h2>
          <p className="auth-description">
            Sign up to raise support tickets and track your issues easily.
          </p>

          <label className="auth-label">Full Name</label>
          <input
            type="text"
            placeholder="Enter your full name"
            onChange={(e) => setName(e.target.value)}
          />

          <label className="auth-label">Email</label>
          <input
            type="text"
            placeholder="Enter your email"
            onChange={(e) => setEmail(e.target.value)}
          />

          <label className="auth-label">Password</label>
          <input
            type="password"
            placeholder="Create a password"
            onChange={(e) => setPassword(e.target.value)}
          />

          <button onClick={register}>Sign Up</button>

          <p onClick={() => navigate("/login")} className="auth-link">
            Already have an account? <span>Login</span>
          </p>
        </div>
      </div>
    </div>
  );
}
