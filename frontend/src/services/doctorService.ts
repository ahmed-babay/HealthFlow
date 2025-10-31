import axios from 'axios';
import { API_BASE_URLS } from '../config/apiConfig';
import authService from './authService';
import { Doctor } from '../types/doctor.types';

// Add JWT token to requests
const getAuthHeaders = () => ({
  headers: {
    Authorization: `Bearer ${authService.getToken()}`,
  },
});

const doctorService = {
  // Get all doctors
  getAllDoctors: async (): Promise<Doctor[]> => {
    const response = await axios.get(
      `${API_BASE_URLS.DOCTOR}/api/v1/doctors`,
      getAuthHeaders()
    );
    return response.data;
  },

  // Get doctor by ID
  getDoctorById: async (id: string) => {
    const response = await axios.get(
      `${API_BASE_URLS.DOCTOR}/api/v1/doctors/${id}`,
      getAuthHeaders()
    );
    return response.data;
  },

  // Search doctors by specialization
  searchBySpecialization: async (specialization: string) => {
    const response = await axios.get(
      `${API_BASE_URLS.DOCTOR}/api/v1/doctors/search/specialization?q=${specialization}`,
      getAuthHeaders()
    );
    return response.data;
  },
};

export default doctorService;
