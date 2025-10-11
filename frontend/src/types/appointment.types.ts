// TypeScript types for appointments

export interface Appointment {
  id: number;
  patientId: number;
  doctorId: string;
  appointmentDateTime: string;
  durationMinutes: number;
  type: AppointmentType;
  status: AppointmentStatus;
  notes?: string;
  createdAt: string;
  updatedAt: string;
}

export type AppointmentType = 
  | 'CONSULTATION'
  | 'FOLLOW_UP'
  | 'EMERGENCY'
  | 'ROUTINE_CHECKUP'
  | 'SPECIALIST_VISIT'
  | 'VACCINATION'
  | 'LAB_TEST'
  | 'IMAGING';

export type AppointmentStatus = 
  | 'SCHEDULED'
  | 'CONFIRMED'
  | 'IN_PROGRESS'
  | 'COMPLETED'
  | 'CANCELLED'
  | 'RESCHEDULED'
  | 'NO_SHOW';

