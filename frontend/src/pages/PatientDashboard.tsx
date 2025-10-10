import { Container } from 'react-bootstrap';
import Navbar from '../components/Navbar';

// Patient Dashboard - Main page for patients
function PatientDashboard() {
  return (
    <>
      {/* Navigation Bar */}
      <Navbar />
      
      {/* Dashboard Content */}
      <Container className="py-5">
        <h1 className="text-white text-center">Patient Dashboard</h1>
        <p className="text-white text-center">Welcome to your dashboard!</p>
      </Container>
    </>
  );
}

export default PatientDashboard;

