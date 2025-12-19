# Implementation Guide - Authentication & Data Entry Features

**Date:** November 18, 2024  
**Session:** Authentication, Child Management, Direct Data Entry, and Google Sheets Sync

## Overview

This guide documents the implementation of four major features:
1. User authentication (login/registration)
2. Child management (add, select, delete children)
3. Direct data entry forms for words, phrases, songs, and letters
4. **Google Sheets sync functionality (retained for admin user)**

## Important Notes

- **Google Sheets functionality is RETAINED** for user_id = 1 (admin user)
- Admin users see an additional "Google Sheets Sync" tab in navigation
- All existing Google Sheets endpoints remain functional
- The sync still works with the default child (child_id = 1) as configured in backend

## Implementation Order

1. ✅ Backend Authentication & User Management (Foundation)
2. ✅ Frontend Login Page (User Entry Point)
3. ✅ Child Management (Add/Select Children)
4. ✅ Direct Data Entry Forms (Core Functionality)

---

## Backend Implementation

### Dependencies to Add

**build.gradle:**
```gradle
implementation 'org.springframework.security:spring-security-crypto:6.1.5'
```

### New Files to Create

#### 1. AuthService.java
- Location: `src/main/java/.../service/AuthService.java`
- Purpose: Handles user registration and login with BCrypt password hashing
- Key methods:
  - `register(username, password, email)` - Creates new user with hashed password
  - `login(username, password)` - Validates credentials
  - `getUserById(userId)` - Retrieves user by ID

#### 2. AuthController.java
- Location: `src/main/java/.../controller/AuthController.java`
- Purpose: REST endpoints for authentication
- Endpoints:
  - `POST /api/auth/register` - User registration
  - `POST /api/auth/login` - User login (stores userId in session)
  - `POST /api/auth/logout` - User logout (invalidates session)
  - `GET /api/auth/me` - Get current logged-in user

#### 3. ChildService.java
- Location: `src/main/java/.../service/ChildService.java`
- Purpose: Child management business logic with ownership verification
- Key methods:
  - `getChildrenByUserId(userId)` - Get all children for a user
  - `getChild(childId, userId)` - Get specific child with ownership check
  - `addChild(userId, childName, birthDate)` - Add new child
  - `updateChild(childId, userId, childName, birthDate)` - Update child
  - `deleteChild(childId, userId)` - Delete child with ownership check

#### 4. ChildController.java
- Location: `src/main/java/.../controller/ChildController.java`
- Purpose: REST endpoints for child management
- Endpoints:
  - `GET /api/children` - Get all children for logged-in user
  - `GET /api/children/{childId}` - Get specific child
  - `POST /api/children` - Add new child
  - `PUT /api/children/{childId}` - Update child
  - `DELETE /api/children/{childId}` - Delete child

#### 5. DataEntryService.java
- Location: `src/main/java/.../service/DataEntryService.java`
- Purpose: CRUD operations for words, phrases, songs, letters with ownership verification
- Key methods for each entity type (Word, Phrase, Song, Letter):
  - `get{Type}s(childId, userId)` - Get all items
  - `add{Type}(childId, userId, data)` - Add new item
  - `update{Type}(itemId, childId, userId, data)` - Update item
  - `delete{Type}(itemId, childId, userId)` - Delete item

#### 6. DataEntryController.java
- Location: `src/main/java/.../controller/DataEntryController.java`
- Purpose: REST endpoints for data entry
- Endpoints (pattern for each type: words, phrases, songs, letters):
  - `GET /api/data/children/{childId}/{type}` - Get all items
  - `POST /api/data/children/{childId}/{type}` - Add item
  - `PUT /api/data/children/{childId}/{type}/{itemId}` - Update item
  - `DELETE /api/data/children/{childId}/{type}/{itemId}` - Delete item

### Repository Updates

Add these methods to existing repository interfaces:

**UserRepository.java:**
```java
Optional<User> findByUsername(String username);
Optional<User> findByEmail(String email);
```

**ChildRepository.java:**
```java
List<Child> findByUser_UserId(Long userId);
```

**WordRepository.java:**
```java
List<Word> findByChild_ChildId(Long childId);
```

**PhraseRepository.java:**
```java
List<Phrase> findByChild_ChildId(Long childId);
```

**SongRepository.java:**
```java
List<Song> findByChild_ChildId(Long childId);
```

**LetterRepository.java:**
```java
List<Letter> findByChild_ChildId(Long childId);
```

### WebConfig Updates (Optional)

Add session configuration to WebConfig.java:
```java
@Bean
public ServletContextInitializer servletContextInitializer() {
    return servletContext -> {
        servletContext.getSessionCookieConfig().setHttpOnly(true);
        servletContext.getSessionCookieConfig().setSecure(false); // true in production
    };
}
```

---

## Frontend Implementation

### File Structure
```
src/
├── App.js (REPLACE)
├── App.css (REPLACE)
├── services/
│   └── api.js (REPLACE)
└── components/ (NEW FOLDER)
    ├── Login.js (NEW)
    ├── Dashboard.js (NEW)
    ├── ChildrenManager.js (NEW)
    ├── DataEntry.js (NEW)
    ├── DataForm.js (NEW)
    └── DataList.js (NEW)
```

### Component Descriptions

#### 1. App.js
- Main application component
- Handles authentication state
- Routes between Login and Dashboard based on auth status
- Checks for existing session on load

#### 2. Login.js
- Login and registration forms
- Toggle between login/register modes
- Auto-login after successful registration
- Error handling and display

#### 3. Dashboard.js
- Main logged-in view
- Header with user info and logout button
- Navigation between "Manage Children" and "Data Entry"
- Loads and manages children list

#### 4. ChildrenManager.js
- Display list of user's children
- Add new child form
- Select child (highlights selected)
- Delete child with confirmation
- Empty state for no children

#### 5. DataEntry.js
- Tab selector for data types (words, phrases, songs, letters)
- Loads appropriate data for selected child
- Toggle between list view and add form
- Handles delete operations with confirmation

#### 6. DataForm.js
- Dynamic form based on data type
- Conditional fields (e.g., date fields appear when checkboxes checked)
- Form validation
- Submit and cancel actions

#### 7. DataList.js
- Display grid of data items
- Formatted display based on data type
- Delete button for each item
- Empty state for no data

### Updated api.js
New endpoints added:
- Authentication: login, register, logout, getCurrentUser
- Children: getChildren, getChild, addChild, updateChild, deleteChild
- Words: getWords, addWord, updateWord, deleteWord
- Phrases: getPhrases, addPhrase, updatePhrase, deletePhrase
- Songs: getSongs, addSong, updateSong, deleteSong
- Letters: getLetters, addLetter, updateLetter, deleteLetter

---

## Security Implementation

### Session-Based Authentication
- **Storage:** User ID stored in HTTP session on server
- **Cookie:** Session ID sent automatically with each request (HttpOnly)
- **Verification:** Every endpoint checks session for userId
- **Authorization:** All data operations verify child ownership

### Security Flow
1. User logs in → Server validates → Stores userId in session → Returns session cookie
2. User makes request → Session cookie sent automatically → Server reads userId from session
3. Server verifies child belongs to user before allowing any operation

### Why This Is Secure
- ✅ User ID never exposed to frontend in manipulable form
- ✅ Session data stored server-side only
- ✅ Cookies are HttpOnly (JavaScript can't access)
- ✅ Every operation verifies ownership
- ✅ No JWT tokens to manage/expire on frontend

---

## Testing Checklist

### Backend Testing
- [ ] Can register new user
- [ ] Can login with correct credentials
- [ ] Login fails with wrong credentials
- [ ] Session persists across requests
- [ ] Can logout and session is invalidated
- [ ] Can add child for logged-in user
- [ ] Can list children for logged-in user
- [ ] Cannot access another user's children
- [ ] Can add/view/delete words for owned child
- [ ] Can add/view/delete phrases for owned child
- [ ] Can add/view/delete songs for owned child
- [ ] Can add/view/delete letters for owned child
- [ ] Cannot modify data for unowned child

### Frontend Testing
- [ ] Registration form works
- [ ] Login form works
- [ ] Toggle between login/register
- [ ] Dashboard loads after login
- [ ] Can add new child
- [ ] Can select different children
- [ ] Can delete child with confirmation
- [ ] Can switch between data types
- [ ] Can add word with all fields
- [ ] Can add phrase with all fields
- [ ] Can add song with all fields
- [ ] Can add letter with all fields
- [ ] Can delete data items
- [ ] Empty states display correctly
- [ ] Logout works and redirects to login
- [ ] Session persists on page refresh

---

## Common Issues & Solutions

### Issue: 401 Unauthorized on requests
**Solution:** Ensure `withCredentials: true` is set in Axios config

### Issue: CORS errors
**Solution:** Verify WebConfig allows credentials and correct origin

### Issue: Session not persisting
**Solution:** Check that cookies are being sent (browser dev tools → Network → Cookies)

### Issue: Can't add child
**Solution:** Verify user is logged in and userId is in session

### Issue: Password too short error
**Solution:** Password must be at least 6 characters

### Issue: Username/email already exists
**Solution:** Check database for existing users or try different credentials

---

## Next Steps (Future Enhancements)

1. **Edit functionality** - Allow editing existing entries (currently only add/delete)
2. **Search and filtering** - Search through entries, filter by date range
3. **Progress tracking** - Visualizations and charts
4. **Google Sheets integration** - Update SheetsController to work with child selection
5. **OAuth integration** - Replace basic auth with OAuth (Google, Facebook, etc.)
6. **Supabase integration** - As mentioned for more robust authentication
7. **Password reset** - Email-based password recovery
8. **Profile settings** - Allow users to update email/password
9. **Data export** - Export child data as PDF or CSV
10. **Mobile responsiveness** - Further optimize for mobile devices

---

## Notes for Tomorrow

- All artifact code has been provided in the conversation above
- Backend files are independent and can be added in any order
- Frontend components depend on each other: App → Dashboard → ChildrenManager/DataEntry → DataForm/DataList
- Start with backend, test with Postman/curl, then add frontend
- Remember to add BCrypt dependency first
- Check that database has proper schema for relationships

---

## Questions to Address Tomorrow

1. Do you want to add edit functionality for existing entries?
2. Should Google Sheets sync be updated to work with selected child?
3. Do you want pagination for large lists of entries?
4. Should there be a confirmation before deleting children (already implemented)?
5. Do you want to keep the old App.js content anywhere for reference?

---

**End of Implementation Guide**  
Resume conversation with: "I've implemented the changes from the guide. Let's continue from here."
