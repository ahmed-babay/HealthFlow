import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import PatientDashboard from './pages/PatientDashboard';
import VideoBackground from './components/VideoBackground';
import './App.css';

function App() {
  return (
    <Router>
      {/* Video background for entire app */}
      <VideoBackground />
      
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
    </Router>
  );
}

export default App;
