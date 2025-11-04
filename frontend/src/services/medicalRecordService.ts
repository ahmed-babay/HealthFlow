import axios from 'axios';
import { API_BASE_URLS } from '../config/apiConfig';
import authService from './authService';
import { MedicalRecord } from '../types/medicalRecord.types';

const getAuthHeaders = () => ({
  headers: {
    Authorization: `Bearer ${authService.getToken()}`,
  },
});

const medicalRecordService = {
  getAllMedicalRecords: async (): Promise<MedicalRecord[]> => {
    const response = await axios.get(
      `${API_BASE_URLS.PATIENT}/medical-records`,
      getAuthHeaders()
    );
    return response.data;
  },

  getMedicalRecordById: async (id: string): Promise<MedicalRecord> => {
    const response = await axios.get(
      `${API_BASE_URLS.PATIENT}/medical-records/${id}`,
      getAuthHeaders()
    );
    return response.data;
  },

  getMedicalRecordsByPatientId: async (patientId: string): Promise<MedicalRecord[]> => {
    const response = await axios.get(
      `${API_BASE_URLS.PATIENT}/medical-records/patient/${patientId}`,
      getAuthHeaders()
    );
    return response.data;
  },

  getMedicalRecordCountByPatient: async (patientId: string): Promise<number> => {
    const response = await axios.get(
      `${API_BASE_URLS.PATIENT}/medical-records/patient/${patientId}/count`,
      getAuthHeaders()
    );
    return response.data;
  },
};

export default medicalRecordService;




