# Toddler Speech Tracker - Project Context

## Project Overview
A full-stack application for tracking toddler speech development, including words, phrases, songs, and letter recognition. The system syncs data from Google Sheets into a local database for tracking and analysis.

## Technology Stack

### Backend
- **Framework**: Spring Boot (Gradle)
- **Language**: Java
- **Database**: JPA/Hibernate (database type not specified in current config)
- **External API**: Google Sheets API v4
- **Logging**: Log4j2
- **Utilities**: Lombok for boilerplate reduction

### Frontend
- **Framework**: React
- **HTTP Client**: Axios
- **Dev Server**: Running on `http://localhost:3000`
- **API Communication**: REST API calls to Spring Boot backend

## Architecture

### Backend Structure
```
com.toddlerspeechtracker.toddlerspeechtracker_gradle/
├── controller/
│   └── SheetsController.java        # REST endpoints for Google Sheets operations
├── service/
│   └── GoogleSheetsService.java     # Google Sheets integration logic
├── model/
│   ├── User.java                    # Parent/caregiver entity
│   ├── Child.java                   # Child entity (can have multiple per user)
│   ├── Word.java                    # Tracked words (signed/verbal)
│   ├── Phrase.java                  # Tracked phrases
│   ├── Song.java                    # Tracked songs
│   └── Letter.java                  # Letter recognition tracking
├── repo/                            # JPA repositories (referenced but not shown)
└── config/
    └── WebConfig.java               # CORS configuration
```

### Frontend Structure
```
src/
├── App.js                           # Main application component
├── services/
│   └── api.js                       # API service layer
└── index.js                         # React entry point
```

## Data Model

### Entity Relationships
- **User** (1) ↔ (Many) **Child**
- **Child** (1) ↔ (Many) **Word**
- **Child** (1) ↔ (Many) **Phrase**
- **Child** (1) ↔ (Many) **Song**
- **Child** (1) ↔ (Many) **Letter**

### Key Entities

#### Word
- Tracks both signed and verbal communication
- Fields: word, signed (boolean), signedDate, verbal (boolean), verbalDate, actualPronunciation, notes, learningSource
- Unique constraint: (child_id, word)

#### Phrase
- Tracks memorable phrases with ratings
- Fields: phrase, dateSaid, funnyRating, cuteRating, learningSource, notes
- Unique constraint: (child_id, phrase)

#### Song
- Tracks songs the child can sing
- Fields: songTitle, dateFirstSang, source, notes
- Unique constraint: (child_id, songTitle)

#### Letter
- Tracks letter recognition and phonics
- Fields: letters, recognized, recognizedDate, soundItOut, soundItOutDate
- Unique constraint: (child_id, letters)

## Current Features

### API Endpoints

#### `/api/fetch` (POST)
- Fetches data from Google Sheets WITHOUT saving to database
- Returns: SyncResult containing lists of Words, Phrases, Songs, Letters
- Use case: Preview/display data before committing

#### `/api/sync` (POST)
- Fetches data from Google Sheets AND saves to database
- Performs upsert operations (update existing, insert new)
- Returns: SyncResult with saved entities
- Use case: Import/sync Google Sheets data to local database

#### `/api/test-connection` (GET)
- Tests Google Sheets API connection
- Returns: Spreadsheet metadata and available sheet tabs

### Google Sheets Integration

#### Configuration
- Uses service account authentication (credentials file)
- Spreadsheet ID configured in `application.properties`
- Default child ID: 1 (configurable via `sheets.defaultChildId`)

#### Sheet Structure
The system expects these named sheets in Google Sheets:
1. **Words** - Columns: word, signed, signedDate, verbal, verbalDate, actualPronunciation, notes, learningSource
2. **Phrases** - Columns: phrase, dateSaid, funnyRating, cuteRating, learningSource, notes
3. **Songs** - Columns: songTitle, dateFirstSang, source, notes
4. **Letters** - Columns: letters, recognized, recognizedDate, soundItOut, soundItOutDate

#### Sync Logic
- **Upsert Strategy**: Updates existing records, inserts new ones
- **Matching Logic**:
    - Words: matched by (child_id, word)
    - Phrases: matched by (child_id, phrase)
    - Songs: matched by (child_id, songTitle)
    - Letters: matched by (child_id, letters)
- **Boolean Parsing**: Accepts "true", "TRUE", "yes", "YES", "Y", "1" as true

### Frontend Features
- Two-button interface:
    - **Fetch**: Display data without saving
    - **Sync**: Save data to database and display
- Data display tables for all four entity types
- Status messages for success/error feedback
- Loading states during API calls

## Configuration Notes

### CORS
- Configured to allow requests from `http://localhost:3000`
- Allows credentials
- Supports GET, POST, PUT, DELETE, OPTIONS methods

### Environment Variables
Backend (`application.properties`):
- `sheets.spreadsheetId` - Google Sheets spreadsheet ID
- `sheets.credentialsFilePath` - Path to service account JSON
- `sheets.defaultChildId` - Default child ID for operations (default: 1)

Frontend (`.env`):
- `REACT_APP_API_URL` - Backend API base URL

## Known Considerations

### Multi-Child Support
- Database schema supports multiple children per user
- Current implementation uses a default child ID (1)
- Future enhancement: User interface to select which child's data to sync

### Authentication
- No authentication/authorization currently implemented
- API endpoints in `api.js` have placeholder login/logout methods
- Future enhancement needed for multi-user support

### Data Validation
- Service layer skips records with null/empty key fields
- No frontend validation currently implemented

### Error Handling
- Backend: Comprehensive try-catch with logging
- Frontend: Error interceptor in Axios, displays error messages to user

## Development Workflow

### Typical Development Session
1. Make changes to code files
2. Test endpoints using frontend UI or API testing tools
3. Verify Google Sheets sync functionality
4. Check logs for any errors or warnings
5. Update CONTEXT.md if architecture changes

### Testing Google Sheets Connection
1. Use `/api/test-connection` endpoint to verify API access
2. Check that all expected sheet tabs are present
3. Verify service account has read access to spreadsheet

## Future Enhancements (Placeholder Endpoints)

### Planned Features
- Direct entry form for speech data (without Google Sheets)
    - `POST /api/speech-entries`
    - `GET /api/speech-entries`
- User authentication system
    - `POST /api/auth/login`
    - `POST /api/auth/logout`
- Child selection interface
- CRUD operations for all entity types
- Search and filtering capabilities
- Progress tracking and visualizations

## Troubleshooting Tips

### Google Sheets Sync Issues
- Verify spreadsheet ID in configuration
- Check service account credentials file path
- Ensure service account has been granted access to spreadsheet
- Verify sheet tab names match exactly: "Words", "Phrases", "Songs", "Letters"
- Check that header row is present (row 1 is skipped during parsing)

### CORS Issues
- Verify frontend is running on `http://localhost:3000`
- Check that `withCredentials: true` is set in Axios
- Ensure WebConfig allows the frontend origin

### Database Issues
- Verify child with ID matching `sheets.defaultChildId` exists in database
- Check entity relationships are properly set up
- Look for constraint violations in logs

## Recent Changes
- Initial project setup completed
- Google Sheets integration implemented with fetch and sync endpoints
- React frontend created with basic data display
- Upsert logic implemented for all entity types
- Multi-child support added to data model (UI pending)

---

**Last Updated**: 2025-11-18  
**Current Status**: Core sync functionality complete, ready for additional features