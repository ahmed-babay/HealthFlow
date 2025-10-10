import { Container } from 'react-bootstrap';
import Navbar from '../components/Navbar';
import WelcomeCard from '../components/WelcomeCard';

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
        
        {/* More content will go here */}
      </Container>
    </>
  );
}

export default PatientDashboard;

