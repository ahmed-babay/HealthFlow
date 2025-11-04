import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Form, Button, Alert } from 'react-bootstrap';
import authService from '../services/authService';
import patientService from '../services/patientService';

// Register form component
function RegisterForm() {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
    firstName: '',
    lastName: '',
    role: 'PATIENT',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  // Handle input changes
  const handleChange = (e: any) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  // Handle form submission
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    // Check if passwords match
    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    setLoading(true);

    try {
      // Call register API
      const response = await authService.register({
        username: formData.username,
        email: formData.email,
        password: formData.password,
        firstName: formData.firstName,
        lastName: formData.lastName,
        role: formData.role,
      });
      
      console.log('Registration successful:', response);
      
      // Create corresponding Patient or Doctor record
      try {
        if (response.role === 'PATIENT') {
          await patientService.createPatient({
            name: `${formData.firstName} ${formData.lastName}`,
            email: formData.email,
            address: 'Not provided', 
            dateOfBirth: '1990-01-01', 
            registeredDate: new Date().toISOString().split('T')[0],
          });
          console.log('Patient record created');
        } else if (response.role === 'DOCTOR') {
          console.log('Doctor registration - doctor record creation pending');
        }
      } catch (profileError: any) {
        console.warn('Failed to create profile record:', profileError);
        // Continue anyway - user can create profile later
      }
      
      // Redirect based on role
      if (response.role === 'DOCTOR') {
        navigate('/doctor-dashboard');
      } else if (response.role === 'ADMIN') {
        navigate('/admin-dashboard');
      } else {
        navigate('/patient-dashboard');
      }
    } catch (err: any) {
      console.error('Registration failed:', err);
      setError(err.response?.data?.message || 'Registration failed. Please try again.');
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
            name="username"
            placeholder="Choose a username"
            value={formData.username}
            onChange={handleChange}
            required
            size="lg"
          />
        </Form.Group>

        {/* First Name */}
        <Form.Group className="mb-3">
          <Form.Label className="fw-semibold">First Name</Form.Label>
          <Form.Control
            type="text"
            name="firstName"
            placeholder="Enter your first name"
            value={formData.firstName}
            onChange={handleChange}
            required
            size="lg"
          />
        </Form.Group>

        {/* Last Name */}
        <Form.Group className="mb-3">
          <Form.Label className="fw-semibold">Last Name</Form.Label>
          <Form.Control
            type="text"
            name="lastName"
            placeholder="Enter your last name"
            value={formData.lastName}
            onChange={handleChange}
            required
            size="lg"
          />
        </Form.Group>
        

        {/* Email */}
        <Form.Group className="mb-3">
          <Form.Label className="fw-semibold">Email</Form.Label>
          <Form.Control
            type="email"
            name="email"
            placeholder="Enter your email"
            value={formData.email}
            onChange={handleChange}
            required
            size="lg"
          />
        </Form.Group>

        {/* Role */}
        <Form.Group className="mb-3">
          <Form.Label className="fw-semibold">I am a...</Form.Label>
          <Form.Select
            name="role"
            value={formData.role}
            onChange={handleChange}
            required
            size="lg"
          >
            <option value="PATIENT">Patient</option>
            <option value="DOCTOR">Doctor</option>
          </Form.Select>
        </Form.Group>

        {/* Password */}
        <Form.Group className="mb-3">
          <Form.Label className="fw-semibold">Password</Form.Label>
          <Form.Control
            type="password"
            name="password"
            placeholder="Create a password"
            value={formData.password}
            onChange={handleChange}
            required
            size="lg"
          />
        </Form.Group>

        {/* Confirm Password */}
        <Form.Group className="mb-4">
          <Form.Label className="fw-semibold">Confirm Password</Form.Label>
          <Form.Control
            type="password"
            name="confirmPassword"
            placeholder="Confirm your password"
            value={formData.confirmPassword}
            onChange={handleChange}
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
          {loading ? 'Creating Account...' : 'Create Account'}
        </Button>
      </Form>
    </>
  );
}

export default RegisterForm;
