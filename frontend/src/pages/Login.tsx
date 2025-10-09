import { Container, Row, Col, Card } from 'react-bootstrap';
import LoginForm from '../components/LoginForm';
import TestCredentials from '../components/TestCredentials';
import Branding from '../components/Branding';

function Login() {
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

          {/* Right Side - Login Form */}
          <Col md={6} className="right-side d-flex align-items-center justify-content-center p-4">
            <Card className="login-card shadow-lg border-0">
              <Card.Body className="p-5">
                
                {/* Header */}
                <div className="text-center mb-4">
                  <h2 className="fw-bold mb-2">Welcome Back</h2>
                  <p className="text-muted">Sign in to your account</p>
                </div>

                {/* Login Form Component */}
                <LoginForm />

                {/* Register Link */}
                <div className="text-center mt-3">
                  <small className="text-muted">
                    Don't have an account?{' '}
                    <a href="/register" className="register-link">Create one</a>
                  </small>
                </div>

                <hr className="my-4" />

                {/* Test Credentials Component */}
                <TestCredentials />

              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  );
}

export default Login;