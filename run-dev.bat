@echo off
echo Starting Toddler Speech Tracker Development Servers...
echo.

start "Backend Server" cmd /k "cd toddlerspeechtracker-gradle && gradlew bootRun"
timeout /t 5 /nobreak > nul

start "Frontend Server" cmd /k "cd toddlerspeechtracker-frontend && npm start"

echo.
echo Both servers are starting...
echo Backend: http://localhost:8080
echo Frontend: http://localhost:3000
echo.