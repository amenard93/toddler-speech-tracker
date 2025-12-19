import React, { useState } from 'react';
import { speechTrackerAPI } from '../services/api';

function ChildrenManager({ children, selectedChild, onChildAdded, onChildDeleted, onChildSelected }) {
  const [showAddForm, setShowAddForm] = useState(false);
  const [formData, setFormData] = useState({
    childName: '',
    birthDate: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await speechTrackerAPI.addChild(formData);
      setFormData({ childName: '', birthDate: '' });
      setShowAddForm(false);
      onChildAdded();
    } catch (err) {
      setError(err.response?.data || 'Failed to add child');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (childId) => {
    if (!window.confirm('Are you sure you want to delete this child? All associated data will be removed.')) {
      return;
    }

    try {
      await speechTrackerAPI.deleteChild(childId);
      onChildDeleted();
    } catch (err) {
      alert('Failed to delete child: ' + (err.response?.data || err.message));
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'Not set';
    const date = new Date(dateString);
    return date.toLocaleDateString();
  };

  return (
    <div className="children-manager">
      <div className="section-header">
        <h2>Your Children</h2>
        <button
          onClick={() => setShowAddForm(!showAddForm)}
          className="btn-primary"
        >
          {showAddForm ? 'Cancel' : '+ Add Child'}
        </button>
      </div>

      {showAddForm && (
        <div className="add-child-form">
          <h3>Add New Child</h3>
          {error && <div className="error-message">{error}</div>}
          
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Child's Name *</label>
              <input
                type="text"
                name="childName"
                value={formData.childName}
                onChange={handleChange}
                required
                disabled={loading}
                placeholder="Enter child's name"
              />
            </div>

            <div className="form-group">
              <label>Birth Date</label>
              <input
                type="date"
                name="birthDate"
                value={formData.birthDate}
                onChange={handleChange}
                disabled={loading}
              />
            </div>

            <button type="submit" className="btn-primary" disabled={loading}>
              {loading ? 'Adding...' : 'Add Child'}
            </button>
          </form>
        </div>
      )}

      {children.length === 0 ? (
        <div className="empty-state">
          <p>No children added yet. Click "Add Child" to get started!</p>
        </div>
      ) : (
        <div className="children-list">
          {children.map((child) => (
            <div
              key={child.childId}
              className={`child-card ${selectedChild?.childId === child.childId ? 'selected' : ''}`}
              onClick={() => onChildSelected(child)}
            >
              <div className="child-info">
                <h3>{child.childName}</h3>
                <p className="birth-date">Born: {formatDate(child.birthDate)}</p>
              </div>
              <button
                onClick={(e) => {
                  e.stopPropagation();
                  handleDelete(child.childId);
                }}
                className="btn-danger"
              >
                Delete
              </button>
            </div>
          ))}
        </div>
      )}

      {selectedChild && (
        <div className="selected-child-info">
          <p>✓ Currently viewing data for: <strong>{selectedChild.childName}</strong></p>
        </div>
      )}
    </div>
  );
}

export default ChildrenManager;