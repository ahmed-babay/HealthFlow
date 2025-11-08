@echo off
REM Healthcare Management System - Start All Services
REM This script starts all microservices in the correct order

echo Starting Healthcare Management System...
echo ==========================================

echo  Starting services...

REM Start Auth Service (must start first)
echo 1️⃣ Starting Auth Service...
start "Auth Service" cmd /k "cd auth-service && mvn spring-boot:run"

REM Wait a bit for Auth Service to start
echo ⏳ Waiting for Auth Service to start...
timeout /t 10 /nobreak >nul

REM Start Patient Service
echo 2️⃣ Starting Patient Service...
start "Patient Service" cmd /k "cd patient-service && mvn spring-boot:run"

REM Wait a bit for Patient Service to start
echo ⏳ Waiting for Patient Service to start...
timeout /t 10 /nobreak >nul

REM Start Doctor Service
echo 3️⃣ Starting Doctor Service...
start "Doctor Service" cmd /k "cd doctor-service && mvn spring-boot:run"

REM Wait a bit for Doctor Service to start
echo ⏳ Waiting for Doctor Service to start...
timeout /t 10 /nobreak >nul

REM Start Appointment Service
echo 4️⃣ Starting Appointment Service...
start "Appointment Service" cmd /k "cd appointment-service && mvn spring-boot:run"

REM Wait a bit for Appointment Service to start
echo ⏳ Waiting for Appointment Service to start...
timeout /t 8 /nobreak >nul

REM Start Frontend (Vite dev server)
echo 5️⃣ Starting Frontend (Vite Dev Server)...
start "Frontend" cmd /k "cd frontend && npm run dev"

echo.
echo All services are starting!
echo ==========================
echo Auth Service:      http://localhost:4002
echo Patient Service:   http://localhost:4000
echo Doctor Service:    http://localhost:4001
echo Appointment Service: http://localhost:4003
echo Frontend (Vite):   http://localhost:5173
echo.
echo Health Checks:
echo curl http://localhost:4000/patients/health
echo curl http://localhost:4001/api/v1/doctors/health
echo curl http://localhost:4002/auth/.well-known/jwks.json
echo curl http://localhost:4003/api/appointments/health
echo.
echo 🧪 Test Files:
echo Use the .http files in api-requests/ directory
echo.
echo 🛑 To stop all services:
echo Close all the command windows that opened
echo.
pause



