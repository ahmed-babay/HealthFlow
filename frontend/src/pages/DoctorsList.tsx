import { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Spinner, Alert } from 'react-bootstrap';
import Navbar from '../components/Navbar';
import doctorService from '../services/doctorService';
import { Doctor } from '../types/doctor.types';

function DoctorsList() {
  const [doctors, setDoctors] = useState<Doctor[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadDoctors();
  }, []);

  const loadDoctors = async () => {
    try {
      const doctorsData = await doctorService.getAllDoctors();
      setDoctors(doctorsData);
    } catch (error: any) {
      console.error('Failed to load doctors:', error);
      setError('Failed to load doctors. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Navbar />
      <Container className="py-5">
        <h2 className="text-white mb-4">Available Doctors</h2>

        {loading && (
          <div className="text-center text-white">
            <Spinner animation="border" role="status">
              <span className="visually-hidden">Loading...</span>
            </Spinner>
            <p className="mt-2">Loading doctors...</p>
          </div>
        )}

        {error && (
          <Alert variant="danger" dismissible onClose={() => setError(null)}>
            {error}
          </Alert>
        )}

        {!loading && !error && (
          <>
            {doctors.length === 0 ? (
              <Card className="shadow-lg border-0" style={{ backgroundColor: 'rgba(255, 255, 255, 0.95)' }}>
                <Card.Body className="text-center p-5">
                  <p className="text-muted mb-0">No doctors available at the moment.</p>
                </Card.Body>
              </Card>
            ) : (
              <Row className="g-4">
                {doctors.map((doctor) => (
                  <Col key={doctor.id} md={6} lg={4}>
                    <Card className="shadow-lg border-0 h-100" style={{ backgroundColor: 'rgba(255, 255, 255, 0.95)' }}>
                      <Card.Body className="p-4">
                        <div className="text-center mb-3">
                          <div
                            className="rounded-circle d-inline-flex align-items-center justify-content-center"
                            style={{
                              width: '60px',
                              height: '60px',
                              backgroundColor: '#007bff',
                              color: 'white',
                              fontSize: '24px',
                            }}
                          >
                            👨‍⚕️
                          </div>
                        </div>
                        <Card.Title className="text-center mb-3">{doctor.name}</Card.Title>
                        <div className="mb-2">
                          <strong>Email:</strong>
                          <br />
                          <a href={`mailto:${doctor.email}`} className="text-decoration-none">
                            {doctor.email}
                          </a>
                        </div>
                        {doctor.specialization && (
                          <div className="mb-2">
                            <strong>Specialization:</strong>
                            <br />
                            <span className="text-muted">{doctor.specialization}</span>
                          </div>
                        )}
                        {doctor.phoneNumber && (
                          <div>
                            <strong>Phone:</strong>
                            <br />
                            <span className="text-muted">{doctor.phoneNumber}</span>
                          </div>
                        )}
                      </Card.Body>
                    </Card>
                  </Col>
                ))}
              </Row>
            )}
          </>
        )}
      </Container>
    </>
  );
}

export default DoctorsList;


