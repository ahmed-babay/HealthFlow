import axios from 'axios';
import { API_BASE_URLS } from '../config/apiConfig';
import authService from './authService';

// Add JWT token to requests
const getAuthHeaders = () => ({
  headers: {
    Authorization: `Bearer ${authService.getToken()}`,
  },
});

const patientService = {
  // Get all patients
  getAllPatients: async () => {
    const response = await axios.get(
      `${API_BASE_URLS.PATIENT}/patients`,
      getAuthHeaders()
    );
    return response.data;
  },

  // Get patient by ID
  getPatientById: async (id: string) => {
    const response = await axios.get(
      `${API_BASE_URLS.PATIENT}/patients/${id}`,
      getAuthHeaders()
    );
    return response.data;
  },
};

export default patientService;

