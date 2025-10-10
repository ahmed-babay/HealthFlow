import { Card, Row, Col } from 'react-bootstrap';
import authService from '../services/authService';

// Welcome card for dashboard
function WelcomeCard() {
  const user = authService.getCurrentUser();

  return (
    <Card className="glass-card mb-4">
      <Card.Body className="p-4">
        <Row>
          <Col>
            <h3 className="text-white mb-2">
              Welcome back, {user?.firstName || user?.username}! 👋
            </h3>
            <p className="text-white-50 mb-0">
              Have a great day managing your health
            </p>
          </Col>
          <Col xs="auto" className="text-end">
            <div className="role-badge">
              {user?.role === 'PATIENT' && '👤 Patient'}
              {user?.role === 'DOCTOR' && '👨‍⚕️ Doctor'}
              {user?.role === 'ADMIN' && '👑 Admin'}
            </div>
          </Col>
        </Row>
      </Card.Body>
    </Card>
  );
}

export default WelcomeCard;

