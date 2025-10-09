import { useState } from 'react';
import { Form, Button, Alert } from 'react-bootstrap';

// This component handles just the login form
function LoginForm() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log('Login:', { username, password });
    alert('Login functionality coming soon!');
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
