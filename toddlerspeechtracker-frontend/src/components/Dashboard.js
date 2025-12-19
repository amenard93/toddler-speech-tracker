import React, { useState, useEffect } from 'react';
import { speechTrackerAPI } from '../services/api';
import ChildrenManager from './ChildrenManager';
import DataEntry from './DataEntry';
import GoogleSheetsSync from './GoogleSheetsSync';

function Dashboard({ user, onLogout }) {
  const [activeTab, setActiveTab] = useState('children');
  const [children, setChildren] = useState([]);
  const [selectedChild, setSelectedChild] = useState(null);
  const [loading, setLoading] = useState(true);

  // Check if user is admin (user_id = 1)
  const isAdmin = user.userId === 1;

  useEffect(() => {
    loadChildren();
  }, []);

  const loadChildren = async () => {
    try {
      setLoading(true);
      const response = await speechTrackerAPI.getChildren();
      setChildren(response.data);
      
      // Auto-select first child if available
      if (response.data.length > 0 && !selectedChild) {
        setSelectedChild(response.data[0]);
      }
    } catch (error) {
      console.error('Error loading children:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleChildAdded = async () => {
    await loadChildren();
  };

  const handleChildDeleted = async () => {
    await loadChildren();
    setSelectedChild(null);
  };

  const handleChildSelected = (child) => {
    setSelectedChild(child);
    // Automatically switch to data entry when child is selected
    if (activeTab === 'children') {
      setActiveTab('data');
    }
  };

  const handleLogout = async () => {
    try {
      await speechTrackerAPI.logout();
      onLogout();
    } catch (error) {
      console.error('Logout error:', error);
      // Logout on frontend anyway
      onLogout();
    }
  };

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <h1>Toddler Speech Tracker</h1>
        <div className="user-info">
          <span>Welcome, {user.username}!</span>
          {isAdmin && <span className="admin-badge">Admin</span>}
          <button onClick={handleLogout} className="btn-secondary">
            Logout
          </button>
        </div>
      </header>

      <nav className="dashboard-nav">
        <button
          className={activeTab === 'children' ? 'active' : ''}
          onClick={() => setActiveTab('children')}
        >
          Manage Children
        </button>
        <button
          className={activeTab === 'data' ? 'active' : ''}
          onClick={() => setActiveTab('data')}
          disabled={!selectedChild}
        >
          Data Entry
        </button>
        {isAdmin && (
          <button
            className={activeTab === 'sheets' ? 'active' : ''}
            onClick={() => setActiveTab('sheets')}
          >
            Google Sheets Sync
          </button>
        )}
      </nav>

      <main className="dashboard-content">
        {loading ? (
          <div className="loading">Loading...</div>
        ) : (
          <>
            {activeTab === 'children' && (
              <ChildrenManager
                children={children}
                selectedChild={selectedChild}
                onChildAdded={handleChildAdded}
                onChildDeleted={handleChildDeleted}
                onChildSelected={handleChildSelected}
              />
            )}

            {activeTab === 'data' && selectedChild && (
              <DataEntry
                child={selectedChild}
                onBack={() => setActiveTab('children')}
              />
            )}

            {activeTab === 'sheets' && isAdmin && (
              <GoogleSheetsSync />
            )}
          </>
        )}
      </main>
    </div>
  );
}

export default Dashboard;