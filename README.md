# Toddler Speech Tracker

Full-stack application for tracking toddler speech development.

## Project Structure

This application consists of two separate repositories:

- **Backend**: [toddlerspeechtracker-backend](https://gitlab.com/YOUR_GITLAB_USERNAME/toddlerspeechtracker-backend)
  - Java Spring Boot application
  - REST API for data management
  - Google Sheets integration
  
- **Frontend**: [toddlerspeechtracker-frontend](https://gitlab.com/YOUR_GITLAB_USERNAME/toddlerspeechtracker-frontend)
  - React application
  - Modern dark-themed UI
  - Data entry and display

## Getting Started

### Backend Setup
```bash
cd toddlerspeechtracker-gradle
./gradlew bootRun
```
Backend runs on `http://localhost:8080`

### Frontend Setup
```bash
cd toddlerspeechtracker-frontend
npm install
npm start
```
Frontend runs on `http://localhost:3000`

### Quick Start
Run both servers at once:
```bash
run-dev.bat
```

## Technologies

- **Backend**: Java, Spring Boot, Gradle
- **Frontend**: React, JavaScript, CSS
- **Database**: [Your database here]
- **APIs**: Google Sheets API