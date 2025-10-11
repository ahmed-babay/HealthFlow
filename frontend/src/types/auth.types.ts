// TypeScript types for authentication

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  role: string;
}

export interface LoginResponse {
  id: string;
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
