import axios from 'axios';
import { API_BASE_URLS } from '../config/apiConfig';
import authService from './authService';

// Add JWT token to requests
const getAuthHeaders = () => ({
  headers: {
    Authorization: `Bearer ${authService.getToken()}`,
  },
});

const appointmentService = {
  // Get all appointments
  getAllAppointments: async () => {
    const response = await axios.get(
      `${API_BASE_URLS.APPOINTMENT}/api/appointments`,
      getAuthHeaders()
    );
    return response.data;
  },

  // Get appointments by patient ID
  getAppointmentsByPatient: async (patientId: number) => {
    const response = await axios.get(
      `${API_BASE_URLS.APPOINTMENT}/api/appointments/patient/${patientId}`,
      getAuthHeaders()
    );
    return response.data;
  },

  // Get upcoming appointments by patient ID
  getUpcomingAppointmentsByPatient: async (patientId: number) => {
    const response = await axios.get(
      `${API_BASE_URLS.APPOINTMENT}/api/appointments/patient/${patientId}/upcoming`,
      getAuthHeaders()
    );
    return response.data;
  },
};

export default appointmentService;
