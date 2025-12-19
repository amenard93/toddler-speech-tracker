import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
});

export const speechTrackerAPI = {
  // Auth endpoints
  login: (credentials) => api.post('/api/auth/login', credentials),
  register: (userData) => api.post('/api/auth/register', userData),
  logout: () => api.post('/api/auth/logout'),
  getCurrentUser: () => api.get('/api/auth/me'),

  // Children endpoints
  getChildren: () => api.get('/api/children'),
  getChild: (childId) => api.get(`/api/children/${childId}`),
  addChild: (childData) => api.post('/api/children', childData),
  updateChild: (childId, childData) => api.put(`/api/children/${childId}`, childData),
  deleteChild: (childId) => api.delete(`/api/children/${childId}`),

  // Word endpoints
  getWords: (childId) => api.get(`/api/data/children/${childId}/words`),
  addWord: (childId, wordData) => api.post(`/api/data/children/${childId}/words`, wordData),
  updateWord: (childId, wordId, wordData) => api.put(`/api/data/children/${childId}/words/${wordId}`, wordData),
  deleteWord: (childId, wordId) => api.delete(`/api/data/children/${childId}/words/${wordId}`),

  // Phrase endpoints
  getPhrases: (childId) => api.get(`/api/data/children/${childId}/phrases`),
  addPhrase: (childId, phraseData) => api.post(`/api/data/children/${childId}/phrases`, phraseData),
  updatePhrase: (childId, phraseId, phraseData) => api.put(`/api/data/children/${childId}/phrases/${phraseId}`, phraseData),
  deletePhrase: (childId, phraseId) => api.delete(`/api/data/children/${childId}/phrases/${phraseId}`),

  // Song endpoints
  getSongs: (childId) => api.get(`/api/data/children/${childId}/songs`),
  addSong: (childId, songData) => api.post(`/api/data/children/${childId}/songs`, songData),
  updateSong: (childId, songId, songData) => api.put(`/api/data/children/${childId}/songs/${songId}`, songData),
  deleteSong: (childId, songId) => api.delete(`/api/data/children/${childId}/songs/${songId}`),

  // Letter endpoints
  getLetters: (childId) => api.get(`/api/data/children/${childId}/letters`),
  addLetter: (childId, letterData) => api.post(`/api/data/children/${childId}/letters`, letterData),
  updateLetter: (childId, letterId, letterData) => api.put(`/api/data/children/${childId}/letters/${letterId}`, letterData),
  deleteLetter: (childId, letterId) => api.delete(`/api/data/children/${childId}/letters/${letterId}`),

  // Existing Google Sheets endpoints
  fetchFromGoogleSheets: () => api.post('/api/fetch'),
  syncToDatabase: () => api.post('/api/sync'),
  testConnection: () => api.get('/api/test-connection'),
};

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      console.error('API Error:', error.response.data);
    } else if (error.request) {
      console.error('Network Error:', error.request);
    } else {
      console.error('Error:', error.message);
    }
    return Promise.reject(error);
  }
);

export default api;