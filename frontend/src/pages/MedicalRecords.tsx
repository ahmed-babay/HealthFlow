import { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Spinner, Alert, Badge } from 'react-bootstrap';
import Navbar from '../components/Navbar';
import medicalRecordService from '../services/medicalRecordService';
import patientService from '../services/patientService';
import authService from '../services/authService';
import { MedicalRecord } from '../types/medicalRecord.types';
import { Patient } from '../types/patient.types';

function MedicalRecords() {
  const [medicalRecords, setMedicalRecords] = useState<MedicalRecord[]>([]);
  const [patient, setPatient] = useState<Patient | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadMedicalRecords();
  }, []);

  const loadMedicalRecords = async () => {
    try {
      const user = authService.getCurrentUser();
      if (!user) {
        setError('Please log in to view medical records.');
        setLoading(false);
        return;
      }

      const patientData = await patientService.getPatientByEmail(user.email);
      setPatient(patientData);

      const records = await medicalRecordService.getMedicalRecordsByPatientId(patientData.id);
      setMedicalRecords(records);
    } catch (error: any) {
      console.error('Failed to load medical records:', error);
      if (error.response?.status === 404) {
        setError('No medical records found.');
      } else {
        setError('Failed to load medical records. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString: string) => {
    if (!dateString) return 'N/A';
    try {
      return new Date(dateString).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
      });
    } catch {
      return dateString;
    }
  };

  return (
    <>
      <Navbar />
      <Container className="py-5">
        <h2 className="text-white mb-4">My Medical Records</h2>

        {loading && (
          <div className="text-center text-white">
            <Spinner animation="border" role="status">
              <span className="visually-hidden">Loading...</span>
            </Spinner>
            <p className="mt-2">Loading medical records...</p>
          </div>
        )}

        {error && (
          <Alert variant="danger" dismissible onClose={() => setError(null)}>
            {error}
          </Alert>
        )}

        {!loading && !error && (
          <>
            {medicalRecords.length === 0 ? (
              <Card className="shadow-lg border-0" style={{ backgroundColor: 'rgba(255, 255, 255, 0.95)' }}>
                <Card.Body className="text-center p-5">
                  <p className="text-muted mb-0">No medical records found.</p>
                </Card.Body>
              </Card>
            ) : (
              <Row className="g-4">
                {medicalRecords.map((record) => (
                  <Col key={record.id} md={12} lg={6}>
                    <Card className="shadow-lg border-0 h-100" style={{ backgroundColor: 'rgba(255, 255, 255, 0.95)' }}>
                      <Card.Body className="p-4">
                        <div className="d-flex justify-content-between align-items-start mb-3">
                          <Card.Title className="mb-0">Medical Record</Card.Title>
                          <Badge bg="primary">{formatDate(record.recordDate)}</Badge>
                        </div>

                        {record.attendingDoctor && (
                          <div className="mb-2">
                            <strong>Attending Doctor:</strong>
                            <p className="text-muted mb-0">{record.attendingDoctor}</p>
                          </div>
                        )}

                        {record.chiefComplaint && (
                          <div className="mb-2">
                            <strong>Chief Complaint:</strong>
                            <p className="text-muted mb-0">{record.chiefComplaint}</p>
                          </div>
                        )}

                        {record.symptoms && (
                          <div className="mb-2">
                            <strong>Symptoms:</strong>
                            <p className="text-muted mb-0">{record.symptoms}</p>
                          </div>
                        )}

                        {record.examinationNotes && (
                          <div className="mb-2">
                            <strong>Examination Notes:</strong>
                            <p className="text-muted mb-0">{record.examinationNotes}</p>
                          </div>
                        )}

                        {record.treatmentPlan && (
                          <div className="mb-2">
                            <strong>Treatment Plan:</strong>
                            <p className="text-muted mb-0">{record.treatmentPlan}</p>
                          </div>
                        )}

                        {record.doctorNotes && (
                          <div className="mb-2">
                            <strong>Doctor Notes:</strong>
                            <p className="text-muted mb-0">{record.doctorNotes}</p>
                          </div>
                        )}

                        <hr className="my-3" />
                        <small className="text-muted">
                          Created: {formatDate(record.createdAt)} | 
                          Updated: {formatDate(record.updatedAt)}
                        </small>
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

export default MedicalRecords;



