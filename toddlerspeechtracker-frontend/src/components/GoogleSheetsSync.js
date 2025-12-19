import React, { useState } from 'react';
import { speechTrackerAPI } from '../services/api';

function GoogleSheetsSync() {
  const [loading, setLoading] = useState(false);
  const [status, setStatus] = useState({ message: '', type: '' });
  const [data, setData] = useState(null);

  const handleFetch = async () => {
    setLoading(true);
    setStatus({ message: 'Fetching from Google Sheets...', type: '' });
    setData(null);

    try {
      const response = await speechTrackerAPI.fetchFromGoogleSheets();
      setStatus({ 
        message: '✓ Fetch complete! Data displayed below (not saved to database).', 
        type: 'success' 
      });
      setData(response.data);
    } catch (error) {
      setStatus({ 
        message: '✗ Error: ' + (error.response?.data || error.message), 
        type: 'error' 
      });
    } finally {
      setLoading(false);
    }
  };

  const handleSync = async () => {
    setLoading(true);
    setStatus({ message: 'Syncing to database...', type: '' });
    setData(null);

    try {
      const response = await speechTrackerAPI.syncToDatabase();
      setStatus({ 
        message: '✓ Sync complete! Data saved to database and displayed below.', 
        type: 'success' 
      });
      setData(response.data);
    } catch (error) {
      setStatus({ 
        message: '✗ Error: ' + (error.response?.data || error.message), 
        type: 'error' 
      });
    } finally {
      setLoading(false);
    }
  };

  const handleTestConnection = async () => {
    setLoading(true);
    setStatus({ message: 'Testing connection...', type: '' });
    setData(null);

    try {
      const response = await speechTrackerAPI.testConnection();
      setStatus({ 
        message: '✓ Connection successful!\n' + response.data, 
        type: 'success' 
      });
    } catch (error) {
      setStatus({ 
        message: '✗ Connection failed: ' + (error.response?.data || error.message), 
        type: 'error' 
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h2>Google Sheets Sync (Admin Only)</h2>
      <p className="info-text">
        This feature syncs data from Google Sheets for the default child (child_id = 1).
        Use "Fetch" to preview data without saving, or "Sync" to save to the database.
      </p>

      <div className="button-group" style={{ marginTop: '20px', marginBottom: '20px' }}>
        <button onClick={handleTestConnection} disabled={loading} className="test-button">
          {loading ? 'Testing...' : 'Test Connection'}
        </button>
        <button onClick={handleFetch} disabled={loading}>
          {loading ? 'Loading...' : 'Fetch from Google Sheets (Display Only)'}
        </button>
        <button onClick={handleSync} disabled={loading} className="sync-button">
          {loading ? 'Loading...' : 'Sync from Google Sheets (Save to Database)'}
        </button>
      </div>

      {status.message && (
        <div className={`status-message ${status.type}`}>
          {status.message.split('\n').map((line, i) => (
            <div key={i}>{line}</div>
          ))}
        </div>
      )}

      {data && <DataDisplay data={data} />}
    </div>
  );
}

// Component to display the data tables
function DataDisplay({ data }) {
  return (
    <div className="sheets-output">
      {data.words && data.words.length > 0 && (
        <DataSection
          title="Words"
          columns={['word', 'signed', 'signedDate', 'verbal', 'verbalDate', 
                   'actualPronunciation', 'notes', 'learningSource']}
          rows={data.words}
        />
      )}

      {data.phrases && data.phrases.length > 0 && (
        <DataSection
          title="Phrases"
          columns={['phrase', 'dateSaid', 'funnyRating', 'cuteRating', 'learningSource', 'notes']}
          rows={data.phrases}
        />
      )}

      {data.songs && data.songs.length > 0 && (
        <DataSection
          title="Songs"
          columns={['songTitle', 'dateFirstSang', 'source', 'notes']}
          rows={data.songs}
        />
      )}

      {data.letters && data.letters.length > 0 && (
        <DataSection
          title="Letters"
          columns={['letters', 'recognized', 'recognizedDate', 'soundItOut', 'soundItOutDate']}
          rows={data.letters}
        />
      )}
    </div>
  );
}

// Component for each data section (Words, Phrases, Songs, Letters)
function DataSection({ title, columns, rows }) {
  return (
    <div className="sheets-section">
      <h3>{title} ({rows.length} records)</h3>
      <div className="table-container">
        <table className="sheets-table">
          <thead>
            <tr>
              {columns.map((col) => (
                <th key={col}>{col}</th>
              ))}
            </tr>
          </thead>
          <tbody>
            {rows.map((row, idx) => (
              <tr key={idx}>
                {columns.map((col) => (
                  <td key={col}>
                    {typeof row[col] === 'boolean' 
                      ? (row[col] ? 'Yes' : 'No')
                      : (row[col] !== null && row[col] !== undefined ? row[col] : '')}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default GoogleSheetsSync;