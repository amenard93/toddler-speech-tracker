import React, { useState, useEffect } from 'react';
import { speechTrackerAPI } from '../services/api';
import DataForm from './DataForm';
import DataList from './DataList';

function DataEntry({ child, onBack }) {
  const [activeType, setActiveType] = useState('words');
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showForm, setShowForm] = useState(false);

  useEffect(() => {
    loadData();
  }, [activeType, child]);

  const loadData = async () => {
    try {
      setLoading(true);
      let response;
      
      switch (activeType) {
        case 'words':
          response = await speechTrackerAPI.getWords(child.childId);
          break;
        case 'phrases':
          response = await speechTrackerAPI.getPhrases(child.childId);
          break;
        case 'songs':
          response = await speechTrackerAPI.getSongs(child.childId);
          break;
        case 'letters':
          response = await speechTrackerAPI.getLetters(child.childId);
          break;
        default:
          response = { data: [] };
      }
      
      setData(response.data);
    } catch (error) {
      console.error('Error loading data:', error);
      setData([]);
    } finally {
      setLoading(false);
    }
  };

  const handleAdd = async (formData) => {
    try {
      switch (activeType) {
        case 'words':
          await speechTrackerAPI.addWord(child.childId, formData);
          break;
        case 'phrases':
          await speechTrackerAPI.addPhrase(child.childId, formData);
          break;
        case 'songs':
          await speechTrackerAPI.addSong(child.childId, formData);
          break;
        case 'letters':
          await speechTrackerAPI.addLetter(child.childId, formData);
          break;
        default:
          break;
      }
      
      setShowForm(false);
      await loadData();
    } catch (error) {
      throw error;
    }
  };

  const handleDelete = async (itemId) => {
    if (!window.confirm('Are you sure you want to delete this item?')) {
      return;
    }

    try {
      switch (activeType) {
        case 'words':
          await speechTrackerAPI.deleteWord(child.childId, itemId);
          break;
        case 'phrases':
          await speechTrackerAPI.deletePhrase(child.childId, itemId);
          break;
        case 'songs':
          await speechTrackerAPI.deleteSong(child.childId, itemId);
          break;
        case 'letters':
          await speechTrackerAPI.deleteLetter(child.childId, itemId);
          break;
        default:
          break;
      }
      
      await loadData();
    } catch (error) {
      alert('Failed to delete: ' + (error.response?.data || error.message));
    }
  };

  return (
    <div className="data-entry">
      <div className="section-header">
        <h2>Data Entry for {child.childName}</h2>
        <button onClick={onBack} className="btn-secondary">
          ← Back to Children
        </button>
      </div>

      <div className="data-type-tabs">
        <button
          className={activeType === 'words' ? 'active' : ''}
          onClick={() => {
            setActiveType('words');
            setShowForm(false);
          }}
        >
          Words
        </button>
        <button
          className={activeType === 'phrases' ? 'active' : ''}
          onClick={() => {
            setActiveType('phrases');
            setShowForm(false);
          }}
        >
          Phrases
        </button>
        <button
          className={activeType === 'songs' ? 'active' : ''}
          onClick={() => {
            setActiveType('songs');
            setShowForm(false);
          }}
        >
          Songs
        </button>
        <button
          className={activeType === 'letters' ? 'active' : ''}
          onClick={() => {
            setActiveType('letters');
            setShowForm(false);
          }}
        >
          Letters
        </button>
      </div>

      <div className="data-actions">
        <button
          onClick={() => setShowForm(!showForm)}
          className="btn-primary"
        >
          {showForm ? 'Cancel' : `+ Add ${activeType.charAt(0).toUpperCase() + activeType.slice(0, -1)}`}
        </button>
      </div>

      {showForm && (
        <DataForm
          type={activeType}
          onSubmit={handleAdd}
          onCancel={() => setShowForm(false)}
        />
      )}

      {loading ? (
        <div className="loading">Loading...</div>
      ) : (
        <DataList
          type={activeType}
          data={data}
          onDelete={handleDelete}
        />
      )}
    </div>
  );
}

export default DataEntry;