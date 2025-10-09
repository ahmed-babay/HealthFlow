// TypeScript types for authentication

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  username: string;
  email: string;
  role: string;
  firstName: string;
  lastName: string;
}

export interface User {
  username: string;
  email: string;
  role: string;
  firstName: string;
  lastName: string;
}

export type UserRole = 'PATIENT' | 'DOCTOR' | 'ADMIN';
