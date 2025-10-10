import { Container, Row, Col } from 'react-bootstrap';
import Navbar from '../components/Navbar';
import WelcomeCard from '../components/WelcomeCard';
import StatsCard from '../components/StatsCard';

// Patient Dashboard - Main page for patients
function PatientDashboard() {
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
              value="3" 
              subtitle="Next: Tomorrow at 10:00 AM"
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

