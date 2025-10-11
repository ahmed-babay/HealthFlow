import { useState, useEffect } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import Navbar from '../components/Navbar';
import WelcomeCard from '../components/WelcomeCard';
import StatsCard from '../components/StatsCard';
import appointmentService from '../services/appointmentService';
import authService from '../services/authService';
import { Appointment } from '../types/appointment.types';

// Patient Dashboard - Main page for patients
function PatientDashboard() {
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [loading, setLoading] = useState(true);

  // Load appointments when component mounts
  useEffect(() => {
    loadAppointments();
  }, []);

  const loadAppointments = async () => {
    try {
      // Get user ID from logged-in user
      const user = authService.getCurrentUser();
      const userId = user?.id ? parseInt(user.id) : 1;
      
      const data = await appointmentService.getUpcomingAppointmentsByPatient(userId);
      setAppointments(data);
      console.log('Loaded appointments:', data);
    } catch (error) {
      console.error('Failed to load appointments:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      {/* Navigation Bar */}
      <Navbar />
      
      {/* Dashboard Content */}
      <Container className="py-4">
        {/* Welcome Card */}
        <WelcomeCard />
        
        {/* Stats Cards */}
        <Row className="g-4 mb-4">
          <Col md={4}>
            <StatsCard 
              icon="📅" 
              title="Upcoming Appointments" 
              value={loading ? '...' : appointments.length} 
              subtitle={loading ? 'Loading...' : `${appointments.length} scheduled`}
            />
          </Col>
          <Col md={4}>
            <StatsCard 
              icon="👨‍⚕️" 
              title="My Doctors" 
              value="2" 
              subtitle="Dr. Smith, Dr. Johnson"
            />
          </Col>
          <Col md={4}>
            <StatsCard 
              icon="📋" 
              title="Medical Records" 
              value="5" 
              subtitle="Last updated: 2 days ago"
            />
          </Col>
        </Row>
        
        {/* More content will go here */}
      </Container>
    </>
  );
}

export default PatientDashboard;

