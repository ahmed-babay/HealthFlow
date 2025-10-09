import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Form, Button, Alert } from 'react-bootstrap';
import authService from '../services/authService';

// This component handles just the login form
function LoginForm() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      // Call login API
      const response = await authService.login({ username, password });
      console.log('Login successful:', response);
      
      // Redirect based on role
      if (response.role === 'DOCTOR') {
        navigate('/doctor-dashboard');
      } else if (response.role === 'ADMIN') {
        navigate('/admin-dashboard');
      } else {
        navigate('/patient-dashboard');
      }
    } catch (err: any) {
      console.error('Login failed:', err);
      setError(err.response?.data?.message || 'Login failed. Please check your credentials.');
      console.log('Login failed:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      {/* Error Message */}
      {error && <Alert variant="danger">{error}</Alert>}

      {/* Form */}
      <Form onSubmit={handleSubmit}>
        
        {/* Username */}
        <Form.Group className="mb-3">
          <Form.Label className="fw-semibold">Username</Form.Label>
          <Form.Control
            type="text"
            placeholder="Enter username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
            size="lg"
          />
        </Form.Group>

        {/* Password */}
        <Form.Group className="mb-4">
          <Form.Label className="fw-semibold">Password</Form.Label>
          <Form.Control
            type="password"
            placeholder="Enter password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            size="lg"
          />
        </Form.Group>

        {/* Submit Button */}
        <Button 
          type="submit" 
          disabled={loading}
          size="lg"
          className="w-100 login-button"
        >
          {loading ? 'Signing in...' : 'Sign In'}
        </Button>
      </Form>
    </>
  );
}

export default LoginForm;
