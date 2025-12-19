import React, { useState } from 'react';

function DataForm({ type, onSubmit, onCancel }) {
  const [formData, setFormData] = useState(getInitialFormData(type));
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  function getInitialFormData(type) {
    switch (type) {
      case 'words':
        return {
          word: '',
          signed: false,
          signedDate: '',
          verbal: false,
          verbalDate: '',
          actualPronunciation: '',
          notes: '',
          learningSource: ''
        };
      case 'phrases':
        return {
          phrase: '',
          dateSaid: '',
          funnyRating: '',
          cuteRating: '',
          learningSource: '',
          notes: ''
        };
      case 'songs':
        return {
          songTitle: '',
          dateFirstSang: '',
          source: '',
          notes: ''
        };
      case 'letters':
        return {
          letters: '',
          recognized: '',
          recognizedDate: '',
          soundItOut: '',
          soundItOutDate: ''
        };
      default:
        return {};
    }
  }

  const handleChange = (e) => {
    const { name, value, type: inputType, checked } = e.target;
    setFormData({
      ...formData,
      [name]: inputType === 'checkbox' ? checked : value
    });
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await onSubmit(formData);
    } catch (err) {
      setError(err.response?.data || 'Failed to save data');
    } finally {
      setLoading(false);
    }
  };

  const renderFormFields = () => {
    switch (type) {
      case 'words':
        return (
          <>
            <div className="form-group">
              <label>Word *</label>
              <input
                type="text"
                name="word"
                value={formData.word}
                onChange={handleChange}
                required
                disabled={loading}
                placeholder="Enter word"
              />
            </div>

            <div className="form-row">
              <div className="form-group checkbox-group">
                <label>
                  <input
                    type="checkbox"
                    name="signed"
                    checked={formData.signed}
                    onChange={handleChange}
                    disabled={loading}
                  />
                  Signed
                </label>
              </div>

              {formData.signed && (
                <div className="form-group">
                  <label>Signed Date</label>
                  <input
                    type="date"
                    name="signedDate"
                    value={formData.signedDate}
                    onChange={handleChange}
                    disabled={loading}
                  />
                </div>
              )}
            </div>

            <div className="form-row">
              <div className="form-group checkbox-group">
                <label>
                  <input
                    type="checkbox"
                    name="verbal"
                    checked={formData.verbal}
                    onChange={handleChange}
                    disabled={loading}
                  />
                  Verbal
                </label>
              </div>

              {formData.verbal && (
                <div className="form-group">
                  <label>Verbal Date</label>
                  <input
                    type="date"
                    name="verbalDate"
                    value={formData.verbalDate}
                    onChange={handleChange}
                    disabled={loading}
                  />
                </div>
              )}
            </div>

            <div className="form-group">
              <label>Actual Pronunciation</label>
              <input
                type="text"
                name="actualPronunciation"
                value={formData.actualPronunciation}
                onChange={handleChange}
                disabled={loading}
                placeholder="How child actually says it"
              />
            </div>

            <div className="form-group">
              <label>Learning Source</label>
              <input
                type="text"
                name="learningSource"
                value={formData.learningSource}
                onChange={handleChange}
                disabled={loading}
                placeholder="Book, TV show, parent, etc."
              />
            </div>

            <div className="form-group">
              <label>Notes</label>
              <textarea
                name="notes"
                value={formData.notes}
                onChange={handleChange}
                disabled={loading}
                placeholder="Any additional notes"
                rows="3"
              />
            </div>
          </>
        );

      case 'phrases':
        return (
          <>
            <div className="form-group">
              <label>Phrase *</label>
              <input
                type="text"
                name="phrase"
                value={formData.phrase}
                onChange={handleChange}
                required
                disabled={loading}
                placeholder="Enter phrase"
              />
            </div>

            <div className="form-group">
              <label>Date Said</label>
              <input
                type="date"
                name="dateSaid"
                value={formData.dateSaid}
                onChange={handleChange}
                disabled={loading}
              />
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Funny Rating (1-10)</label>
                <input
                  type="number"
                  name="funnyRating"
                  value={formData.funnyRating}
                  onChange={handleChange}
                  min="1"
                  max="10"
                  disabled={loading}
                />
              </div>

              <div className="form-group">
                <label>Cute Rating (1-10)</label>
                <input
                  type="number"
                  name="cuteRating"
                  value={formData.cuteRating}
                  onChange={handleChange}
                  min="1"
                  max="10"
                  disabled={loading}
                />
              </div>
            </div>

            <div className="form-group">
              <label>Learning Source</label>
              <input
                type="text"
                name="learningSource"
                value={formData.learningSource}
                onChange={handleChange}
                disabled={loading}
                placeholder="Book, TV show, parent, etc."
              />
            </div>

            <div className="form-group">
              <label>Notes</label>
              <textarea
                name="notes"
                value={formData.notes}
                onChange={handleChange}
                disabled={loading}
                placeholder="Any additional notes"
                rows="3"
              />
            </div>
          </>
        );

      case 'songs':
        return (
          <>
            <div className="form-group">
              <label>Song Title *</label>
              <input
                type="text"
                name="songTitle"
                value={formData.songTitle}
                onChange={handleChange}
                required
                disabled={loading}
                placeholder="Enter song title"
              />
            </div>

            <div className="form-group">
              <label>Date First Sang</label>
              <input
                type="date"
                name="dateFirstSang"
                value={formData.dateFirstSang}
                onChange={handleChange}
                disabled={loading}
              />
            </div>

            <div className="form-group">
              <label>Source</label>
              <input
                type="text"
                name="source"
                value={formData.source}
                onChange={handleChange}
                disabled={loading}
                placeholder="Where they learned it"
              />
            </div>

            <div className="form-group">
              <label>Notes</label>
              <textarea
                name="notes"
                value={formData.notes}
                onChange={handleChange}
                disabled={loading}
                placeholder="Any additional notes"
                rows="3"
              />
            </div>
          </>
        );

      case 'letters':
        return (
          <>
            <div className="form-group">
              <label>Letter(s) *</label>
              <input
                type="text"
                name="letters"
                value={formData.letters}
                onChange={handleChange}
                required
                disabled={loading}
                placeholder="A, B, C, etc."
              />
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Recognized?</label>
                <select
                  name="recognized"
                  value={formData.recognized}
                  onChange={handleChange}
                  disabled={loading}
                >
                  <option value="">Select...</option>
                  <option value="yes">Yes</option>
                  <option value="no">No</option>
                </select>
              </div>

              <div className="form-group">
                <label>Recognized Date</label>
                <input
                  type="date"
                  name="recognizedDate"
                  value={formData.recognizedDate}
                  onChange={handleChange}
                  disabled={loading}
                />
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Can Sound It Out?</label>
                <select
                  name="soundItOut"
                  value={formData.soundItOut}
                  onChange={handleChange}
                  disabled={loading}
                >
                  <option value="">Select...</option>
                  <option value="yes">Yes</option>
                  <option value="no">No</option>
                </select>
              </div>

              <div className="form-group">
                <label>Sound It Out Date</label>
                <input
                  type="date"
                  name="soundItOutDate"
                  value={formData.soundItOutDate}
                  onChange={handleChange}
                  disabled={loading}
                />
              </div>
            </div>
          </>
        );

      default:
        return null;
    }
  };

  return (
    <div className="data-form">
      <h3>Add New {type.charAt(0).toUpperCase() + type.slice(0, -1)}</h3>
      {error && <div className="error-message">{error}</div>}
      
      <form onSubmit={handleSubmit}>
        {renderFormFields()}

        <div className="form-actions">
          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? 'Saving...' : 'Save'}
          </button>
          <button
            type="button"
            onClick={onCancel}
            className="btn-secondary"
            disabled={loading}
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}

export default DataForm;