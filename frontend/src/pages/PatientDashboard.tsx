import { useState, useEffect } from 'react';
import { Container, Row, Col, Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';
import WelcomeCard from '../components/WelcomeCard';
import StatsCard from '../components/StatsCard';
import appointmentService from '../services/appointmentService';
import doctorService from '../services/doctorService';
import patientService from '../services/patientService';
import authService from '../services/authService';
import { Appointment } from '../types/appointment.types';
import { Doctor } from '../types/doctor.types';
import { Patient } from '../types/patient.types';

function PatientDashboard() {
  const navigate = useNavigate();
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [doctors, setDoctors] = useState<Doctor[]>([]);
  const [patient, setPatient] = useState<Patient | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    try {
      const user = authService.getCurrentUser();
      if (!user) {
        console.error('❌ No user found in localStorage');
        setLoading(false);
        return;
      }

      console.log('🔄 Fetching patient data from backend...');
      let currentPatient: Patient | null = null;
      try {
        currentPatient = await patientService.getPatientByEmail(user.email);
        setPatient(currentPatient);
        console.log('✅ Successfully loaded patient:', currentPatient);
      } catch (error: any) {
        console.error('❌ Failed to load patient:', error);
        if (error.response?.status === 404) {
          console.warn('⚠️ Patient record not found. User might need to create a patient profile.');
        }
      }

      console.log('🔄 Fetching doctors from backend...');
      try {
        const doctorsData = await doctorService.getAllDoctors();
        setDoctors(doctorsData);
        console.log('✅ Successfully loaded doctors:', doctorsData);
        console.log(`📊 Total doctors count: ${doctorsData.length}`);
      } catch (error: any) {
        console.error('❌ Failed to load doctors:', error);
      }
      
      console.log('🔄 Fetching appointments from backend...');
      try {
        const patientIdForAppointments = currentPatient ? 1 : 1;
        const appointmentsData = await appointmentService.getUpcomingAppointmentsByPatient(patientIdForAppointments);
        setAppointments(appointmentsData);
        console.log('✅ Successfully loaded appointments:', appointmentsData);
        console.log(`📊 Total appointments count: ${appointmentsData.length}`);
      } catch (error: any) {
        console.error('❌ Failed to load appointments:', error);
        if (error.response) {
          console.error('Response status:', error.response.status);
          console.error('Response data:', error.response.data);
        }
        setAppointments([]);
      }
      
    } catch (error: any) {
      console.error('❌ Failed to load dashboard data:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Navbar />
      
      <Container className="py-4">
        {loading && (
          <div className="text-center text-white mb-4">
            <div className="spinner-border" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
            <p className="mt-2">Loading dashboard data...</p>
          </div>
        )}
        
        <WelcomeCard />
        
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
              title="Available Doctors" 
              value={loading ? '...' : doctors.length} 
              subtitle={loading ? 'Loading...' : `${doctors.length} doctors available`}
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

        <Row className="g-4">
          <Col md={4}>
            <div className="d-grid">
              <Button
                variant="primary"
                size="lg"
                onClick={() => navigate('/edit-profile')}
                className="py-3"
              >
                ✏️ Edit Personal Data
              </Button>
            </div>
          </Col>
          <Col md={4}>
            <div className="d-grid">
              <Button
                variant="success"
                size="lg"
                onClick={() => navigate('/doctors')}
                className="py-3"
              >
                👨‍⚕️ View Available Doctors
              </Button>
            </div>
          </Col>
          <Col md={4}>
            <div className="d-grid">
              <Button
                variant="info"
                size="lg"
                onClick={() => navigate('/medical-records')}
                className="py-3"
              >
                📋 View Medical Records
              </Button>
            </div>
          </Col>
        </Row>
      </Container>
    </>
  );
}

export default PatientDashboard;

