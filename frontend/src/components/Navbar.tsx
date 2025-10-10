import { Navbar as BootstrapNavbar, Container, Nav, Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import authService from '../services/authService';

// Navigation bar component
function Navbar() {
  const navigate = useNavigate();
  const user = authService.getCurrentUser();

  // Handle logout
  const handleLogout = () => {
    authService.logout();
    navigate('/login');
  };

  return (
    <BootstrapNavbar className="custom-navbar" expand="lg">
      <Container>
        {/* Logo/Brand */}
        <BootstrapNavbar.Brand href="#" className="text-white fw-bold">
           HealthFlow
        </BootstrapNavbar.Brand>

        {/* User info and logout */}
        <Nav className="ms-auto align-items-center">
          <span className="text-white me-3">
          </span>
          <Button 
            variant="outline-light" 
            size="sm"
            onClick={handleLogout}
          >
            Logout
          </Button>
        </Nav>
      </Container>
    </BootstrapNavbar>
  );
}

export default Navbar;


