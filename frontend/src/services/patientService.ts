import axios from 'axios';
import { API_BASE_URLS } from '../config/apiConfig';
import authService from './authService';

const getAuthHeaders = () => ({
  headers: {
    Authorization: `Bearer ${authService.getToken()}`,
  },
});

const patientService = {
  getAllPatients: async () => {
    const response = await axios.get(
      `${API_BASE_URLS.PATIENT}/patients`,
      getAuthHeaders()
    );
    return response.data;
  },

  getPatientById: async (id: string) => {
    const response = await axios.get(
      `${API_BASE_URLS.PATIENT}/patients/${id}`,
      getAuthHeaders()
    );
    return response.data;
  },

  getPatientByEmail: async (email: string) => {
    const response = await axios.get(
      `${API_BASE_URLS.PATIENT}/patients/search/email?email=${encodeURIComponent(email)}`,
      getAuthHeaders()
    );
    return response.data;
  },
};

export default patientService;

