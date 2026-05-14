import logo from "./logo.svg";
import "./App.css";
import { BrowserRouter, Route, Router, Routes } from "react-router-dom";
import Dashboard from "./Pages/Customer/Dashboard";
import CreateTicket from "./Pages/Customer/CreateTicket";
import MyTickets from "./Pages/Customer/MyTickets";
import TicketView from "./Pages/Customer/TicketView";
import Inbox from "./Pages/Agent/Inbox";
import Overview from "./Pages/Analytics/Overview";
import RoutingRules from "./Pages/Admin/RoutingRules";
import KbList from "./Pages/Kb/KbList";
import KbCreate from "./Pages/Kb/KbCreate";
import KbSearch from "./Pages/Kb/KbSearch";
import SlaManager from "./Pages/Admin/SlaManager";
import AgentTicketView from "./Pages/Agent/AgentTicketView";
import Navbar from "./components/Navbar/Navbar";
import Login from "./Pages/Login";
import Signup from "./Pages/Signup";
import { AuthProvider } from "./Auth/AuthContext";
import ProtectedRoute from "./Auth/ProtectedRoute";
import AgentRoute from "./Auth/AgentRoute";
import AgentDashboard from "./Pages/Agent/AgentDashboard";
import UnassignedTickets from "./Pages/Agent/UnassignedTickets";
import AgentKbHub from "./Pages/Kb/AgentKbHub";
import KbDetail from "./Pages/Kb/KbDetail";
function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <div className="PageWrapper">
          <Navbar />
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<Signup />} />
            {/* Customer portal */}
            <Route path="/" element={<Dashboard />} />
            <Route
              path="/create-ticket"
              element={
                <ProtectedRoute>
                  <CreateTicket />
                </ProtectedRoute>
              }
            />
            <Route
              path="/tickets"
              element={
                <ProtectedRoute>
                  <MyTickets />
                </ProtectedRoute>
              }
            />
            <Route
              path="/ticket/:id"
              element={
                <ProtectedRoute>
                  <TicketView />
                </ProtectedRoute>
              }
            />

            {/* Agent Portal */}
            <Route
              path="/agent/inbox"
              element={
                <AgentRoute>
                  <Inbox />
                </AgentRoute>
              }
            />
            <Route
              path="/agent/unassigned"
              element={
                <AgentRoute>
                  <UnassignedTickets />
                </AgentRoute>
              }
            />
            <Route
              path="/agent/tickets/:id"
              element={
                <AgentRoute>
                  <AgentTicketView />
                </AgentRoute>
              }
            />
            <Route
              path="/agent/dashboard"
              element={
                <AgentRoute>
                  <AgentDashboard />
                </AgentRoute>
              }
            />

            {/* Knowledge Base */}
            <Route path="/kb" element={<KbList />} />
            <Route path="/kb/create" element={<KbCreate />} />
            <Route path="/kb/search" element={<KbSearch />} />
            <Route path="/kb/article/:id" element={<KbDetail />} />
            <Route
              path="/agent/kb"
              element={
                <AgentRoute>
                  <AgentKbHub />
                </AgentRoute> 
              }
            />

            {/* Analytics */}
            <Route path="/analytics" element={<Overview />} />

            {/* Admin Settings */}
            <Route path="/admin/sla" element={<SlaManager />} />
            <Route path="/admin/routing" element={<RoutingRules />} />

            <Route path="/agent/tickets/:id" element={<AgentTicketView />} />
          </Routes>
        </div>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
