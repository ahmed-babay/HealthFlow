import { Container, Row, Col, Card } from 'react-bootstrap';
import RegisterForm from '../components/RegisterForm';
import Branding from '../components/Branding';

function Register() {
  return (
    <div className="login-page">
      <Container fluid className="h-100">
        <Row className="h-100">
          
          {/* Left Side - Branding */}
          <Col 
            md={6} 
            className="left-side d-none d-md-flex flex-column align-items-center justify-content-center text-white"
          >
            <Branding />
          </Col>

          {/* Right Side - Register Form */}
          <Col md={6} className="right-side d-flex align-items-center justify-content-center p-4">
            <Card className="login-card shadow-lg border-0">
              <Card.Body className="p-5">
                
                {/* Header */}
                <div className="text-center mb-4">
                  <h2 className="fw-bold mb-2">Create Account</h2>
                  <p className="text-muted">Join HealthFlow today</p>
                </div>

                {/* Register Form Component */}
                <RegisterForm />

                {/* Login Link */}
                <div className="text-center mt-3">
                  <small className="text-muted">
                    Already have an account?{' '}
                    <a href="/login" className="register-link">Sign in</a>
                  </small>
                </div>

              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  );
}

export default Register;


