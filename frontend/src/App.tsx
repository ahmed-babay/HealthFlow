import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route path="/login" element={<h1>Login Page - Coming Soon</h1>} />
          <Route path="/register" element={<h1>Register Page - Coming Soon</h1>} />
          <Route path="/patient-dashboard" element={<h1>Patient Dashboard - Coming Soon</h1>} />
          <Route path="/doctor-dashboard" element={<h1>Doctor Dashboard - Coming Soon</h1>} />
          <Route path="/admin-dashboard" element={<h1>Admin Dashboard - Coming Soon</h1>} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
