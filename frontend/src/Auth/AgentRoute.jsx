import React from 'react'
import { useAuth } from './AuthContext';
import { Navigate } from 'react-router-dom';

export default function AgentRoute({children}) {
  const {user} = useAuth();
    if (!user) {
        return <Navigate to="/login" replace />;
    }

    if (user.role !== "AGENT" && user.role !== "SUPERVISOR" ) {
        return <Navigate to="/" replace />;
    }
    return children;
}
