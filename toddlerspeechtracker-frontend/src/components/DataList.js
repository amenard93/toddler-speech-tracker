import React from 'react';

function DataList({ type, data, onDelete }) {
  const formatDate = (dateString) => {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString();
  };

  const renderItem = (item) => {
    const getItemId = () => {
      switch (type) {
        case 'words': return item.wordId;
        case 'phrases': return item.phraseId;
        case 'songs': return item.songId;
        case 'letters': return item.letterId;
        default: return null;
      }
    };

    const itemId = getItemId();

    switch (type) {
      case 'words':
        return (
          <div key={itemId} className="data-item">
            <div className="item-header">
              <h4>{item.word}</h4>
              <button
                onClick={() => onDelete(itemId)}
                className="btn-danger-small"
              >
                Delete
              </button>
            </div>
            <div className="item-details">
              <div className="detail-row">
                <span className="label">Signed:</span>
                <span>{item.signed ? '✓ Yes' : '✗ No'}</span>
                {item.signed && item.signedDate && (
                  <>
                    <span className="label">Date:</span>
                    <span>{formatDate(item.signedDate)}</span>
                  </>
                )}
              </div>
              <div className="detail-row">
                <span className="label">Verbal:</span>
                <span>{item.verbal ? '✓ Yes' : '✗ No'}</span>
                {item.verbal && item.verbalDate && (
                  <>
                    <span className="label">Date:</span>
                    <span>{formatDate(item.verbalDate)}</span>
                  </>
                )}
              </div>
              {item.actualPronunciation && (
                <div className="detail-row">
                  <span className="label">Pronunciation:</span>
                  <span>{item.actualPronunciation}</span>
                </div>
              )}
              {item.learningSource && (
                <div className="detail-row">
                  <span className="label">Source:</span>
                  <span>{item.learningSource}</span>
                </div>
              )}
              {item.notes && (
                <div className="detail-row full-width">
                  <span className="label">Notes:</span>
                  <span>{item.notes}</span>
                </div>
              )}
            </div>
          </div>
        );

      case 'phrases':
        return (
          <div key={itemId} className="data-item">
            <div className="item-header">
              <h4>"{item.phrase}"</h4>
              <button
                onClick={() => onDelete(itemId)}
                className="btn-danger-small"
              >
                Delete
              </button>
            </div>
            <div className="item-details">
              {item.dateSaid && (
                <div className="detail-row">
                  <span className="label">Date Said:</span>
                  <span>{formatDate(item.dateSaid)}</span>
                </div>
              )}
              <div className="detail-row">
                {item.funnyRating && (
                  <>
                    <span className="label">Funny:</span>
                    <span>{item.funnyRating}/10</span>
                  </>
                )}
                {item.cuteRating && (
                  <>
                    <span className="label">Cute:</span>
                    <span>{item.cuteRating}/10</span>
                  </>
                )}
              </div>
              {item.learningSource && (
                <div className="detail-row">
                  <span className="label">Source:</span>
                  <span>{item.learningSource}</span>
                </div>
              )}
              {item.notes && (
                <div className="detail-row full-width">
                  <span className="label">Notes:</span>
                  <span>{item.notes}</span>
                </div>
              )}
            </div>
          </div>
        );

      case 'songs':
        return (
          <div key={itemId} className="data-item">
            <div className="item-header">
              <h4>{item.songTitle}</h4>
              <button
                onClick={() => onDelete(itemId)}
                className="btn-danger-small"
              >
                Delete
              </button>
            </div>
            <div className="item-details">
              {item.dateFirstSang && (
                <div className="detail-row">
                  <span className="label">First Sang:</span>
                  <span>{formatDate(item.dateFirstSang)}</span>
                </div>
              )}
              {item.source && (
                <div className="detail-row">
                  <span className="label">Source:</span>
                  <span>{item.source}</span>
                </div>
              )}
              {item.notes && (
                <div className="detail-row full-width">
                  <span className="label">Notes:</span>
                  <span>{item.notes}</span>
                </div>
              )}
            </div>
          </div>
        );

      case 'letters':
        return (
          <div key={itemId} className="data-item">
            <div className="item-header">
              <h4>Letter: {item.letters}</h4>
              <button
                onClick={() => onDelete(itemId)}
                className="btn-danger-small"
              >
                Delete
              </button>
            </div>
            <div className="item-details">
              <div className="detail-row">
                <span className="label">Recognized:</span>
                <span>{item.recognized || 'Not set'}</span>
                {item.recognizedDate && (
                  <>
                    <span className="label">Date:</span>
                    <span>{formatDate(item.recognizedDate)}</span>
                  </>
                )}
              </div>
              <div className="detail-row">
                <span className="label">Can Sound Out:</span>
                <span>{item.soundItOut || 'Not set'}</span>
                {item.soundItOutDate && (
                  <>
                    <span className="label">Date:</span>
                    <span>{formatDate(item.soundItOutDate)}</span>
                  </>
                )}
              </div>
            </div>
          </div>
        );

      default:
        return null;
    }
  };

  if (data.length === 0) {
    return (
      <div className="empty-state">
        <p>No {type} added yet. Click the "Add" button to get started!</p>
      </div>
    );
  }

  return (
    <div className="data-list">
      <h3>{type.charAt(0).toUpperCase() + type.slice(1)} ({data.length})</h3>
      <div className="items-grid">
        {data.map(item => renderItem(item))}
      </div>
    </div>
  );
}

export default DataList;