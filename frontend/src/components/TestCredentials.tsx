// Component to display test login credentials
function TestCredentials() {
  return (
    <div className="test-credentials bg-light p-3 rounded">
      <p className="small text-uppercase fw-semibold text-muted mb-2">
        Test Credentials
      </p>
      
      <div className="d-flex justify-content-between mb-2">
        <span>👨‍⚕️ Doctor</span>
        <code>doctor_user / password123</code>
      </div>
      
      <div className="d-flex justify-content-between mb-2">
        <span>👤 Patient</span>
        <code>patient_user / password123</code>
      </div>
      
      <div className="d-flex justify-content-between">
        <span>👑 Admin</span>
        <code>admin_user / password123</code>
      </div>
    </div>
  );
}

export default TestCredentials;
