import axios from 'axios';
import { API_BASE_URLS } from '../config/apiConfig';
import { LoginRequest, LoginResponse } from '../types/auth.types';

// Authentication Service - handles all auth-related API calls
const authService = {
  
  // Login user
  login: async (credentials: LoginRequest): Promise<LoginResponse> => {
    const response = await axios.post<LoginResponse>(
      `${API_BASE_URLS.AUTH}/auth/login`,
      credentials
    );
    
    // Save JWT token to localStorage
    if (response.data.token) {
      localStorage.setItem('jwt_token', response.data.token);
      localStorage.setItem('user', JSON.stringify(response.data));
    }
    else{
      throw new Error('Login failed');
    }
    
    return response.data;
  },

  // Logout user
  logout: (): void => {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('user');
  },

  // Get JWT token
  getToken: (): string | null => {
    return localStorage.getItem('jwt_token');
  },

  // Get current user
  getCurrentUser: (): LoginResponse | null => {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  },

  // Check if user is authenticated
  isAuthenticated: (): boolean => {
    return !!localStorage.getItem('jwt_token');
  },
};

export default authService;
