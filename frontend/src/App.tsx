import { BrowserRouter as Router, Routes, Route, Navigate, useLocation } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import PatientDashboard from './pages/PatientDashboard';
import VideoBackground from './components/VideoBackground';
import './App.css';

function AppContent() {
  const location = useLocation();
  
  // Show video only on login and register pages
  const showVideo = location.pathname === '/login' || location.pathname === '/register' || location.pathname === '/';
  
  return (
    <>
      {/* Video background only for login/register */}
      {showVideo && <VideoBackground />}
      
      <div className="App">
        <Routes>
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/patient-dashboard" element={<PatientDashboard />} />
          <Route path="/doctor-dashboard" element={<h1>Doctor Dashboard - Coming Soon</h1>} />
          <Route path="/admin-dashboard" element={<h1>Admin Dashboard - Coming Soon</h1>} />
        </Routes>
      </div>
    </>
  );
}

function App() {
  return (
    <Router>
      <AppContent />
    </Router>
  );
}

export default App;
