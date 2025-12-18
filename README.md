# Toddler Speech Tracker

Full-stack application for tracking toddler speech development with secure authentication and Google Sheets integration.

## 🏗️ Project Structure
```
toddlerspeechtracker/
├── toddlerspeechtracker-gradle/     # Spring Boot backend
└── toddlerspeechtracker-frontend/   # React frontend
```

## 🚀 Technologies

**Backend:**
- Java 17
- Spring Boot 3.x
- JPA/Hibernate
- PostgreSQL
- Google Sheets API
- OAuth 2.0
- BCrypt

**Frontend:**
- React 18
- Axios
- React Hooks
- CSS3

## ⚙️ Setup Instructions

### Prerequisites
- Java 17+
- Node.js 16+
- PostgreSQL 12+
- Google Cloud Platform account

### Backend Setup

1. **Navigate to backend directory**
```bash
   cd toddlerspeechtracker-gradle
```

2. **Configure database**
```bash
   createdb toddler_speech_tracker
```

3. **Set up application properties**
```bash
   cp application.properties.example application.properties
   nano application.properties
   # Update with your database credentials and Google Sheets ID
```

4. **Set up Google Sheets API**
   - Go to [Google Cloud Console](https://console.cloud.google.com/)
   - Create a new project
   - Enable Google Sheets API
   - Create a service account
   - Download JSON credentials
   - Create folder: `src/main/resources/credentials/`
   - Place credentials as: `src/main/resources/credentials/google-service-account.json`
   - Share your Google Sheet with the service account email

5. **Run backend**
```bash
   ./gradlew bootRun
```
   Backend runs on `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend directory**
```bash
   cd toddlerspeechtracker-frontend
```

2. **Install dependencies**
```bash
   npm install
```

3. **Configure environment**
```bash
   cp .env.example .env
```

4. **Start frontend**
```bash
   npm start
```
   Frontend runs on `http://localhost:3000`

## 📱 Features

- **User Authentication:** Secure registration and login with BCrypt password hashing
- **Multi-Child Management:** Track development for multiple children per user
- **Speech Tracking:**
  - Words (signed and verbal with dates)
  - Phrases (with funny/cute ratings)
  - Songs (with sources and notes)
  - Letter Recognition (recognition and phonics)
- **Google Sheets Integration:** Bidirectional sync for admin users
- **Role-Based Access Control:** Admin-only features
- **Ownership Verification:** Users can only access their own data

## 🔒 Security Features

- BCrypt password hashing
- Session-based authentication with HTTP-only cookies
- CORS configuration
- SQL injection prevention
- OAuth 2.0 for Google Sheets API
- Input validation and sanitization

## 📊 Database Schema

- `users` - User accounts with encrypted passwords
- `children` - Child profiles linked to users
- `word` - Tracked words with signed/verbal indicators
- `phrase` - Tracked phrases with ratings
- `song` - Tracked songs with sources
- `letter` - Letter recognition tracking

## 🎯 API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout
- `GET /api/auth/me` - Get current user

### Children Management
- `GET /api/children` - Get all children for user
- `POST /api/children` - Add new child
- `PUT /api/children/{id}` - Update child
- `DELETE /api/children/{id}` - Delete child

### Data Entry (Words, Phrases, Songs, Letters)
- `GET /api/data/children/{childId}/{type}` - Get all items
- `POST /api/data/children/{childId}/{type}` - Add item
- `PUT /api/data/children/{childId}/{type}/{itemId}` - Update item
- `DELETE /api/data/children/{childId}/{type}/{itemId}` - Delete item

### Google Sheets Sync (Admin Only)
- `POST /api/fetch` - Fetch from Google Sheets (preview)
- `POST /api/sync` - Sync from Google Sheets (save to DB)
- `GET /api/test-connection` - Test Google Sheets connection

## 🧪 Testing

Backend includes:
- JUnit tests
- Mockito for mocking
- SonarQube integration
- 85%+ code coverage target

## 📝 License

MIT License

## 👤 Author

**Andrew Menard**
- LinkedIn: [linkedin.com/in/amenard93](https://linkedin.com/in/amenard93)
- GitHub: [github.com/amenard93](https://github.com/amenard93)
- Email: menard.andrewg@gmail.com

## 🙏 Acknowledgments

Built as a personal project to demonstrate full-stack development capabilities with Spring Boot and React.
