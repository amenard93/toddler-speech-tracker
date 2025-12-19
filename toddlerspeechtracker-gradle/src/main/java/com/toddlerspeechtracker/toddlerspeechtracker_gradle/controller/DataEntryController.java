package com.toddlerspeechtracker.toddlerspeechtracker_gradle.controller;

import com.toddlerspeechtracker.toddlerspeechtracker_gradle.model.*;
import com.toddlerspeechtracker.toddlerspeechtracker_gradle.service.DataEntryService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/data")
public class DataEntryController {

    private final DataEntryService dataEntryService;

    public DataEntryController(DataEntryService dataEntryService) {
        this.dataEntryService = dataEntryService;
    }

    // ========== WORD ENDPOINTS ==========

    @GetMapping("/children/{childId}/words")
    public ResponseEntity<?> getWords(@PathVariable Long childId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        try {
            List<Word> words = dataEntryService.getWords(childId, userId);
            return ResponseEntity.ok(words);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error fetching words", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching words"));
        }
    }

    @PostMapping("/children/{childId}/words")
    public ResponseEntity<?> addWord(
            @PathVariable Long childId,
            @RequestBody Word word,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        try {
            Word savedWord = dataEntryService.addWord(childId, userId, word);
            return ResponseEntity.ok(savedWord);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error adding word", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error adding word"));
        }
    }

    @PutMapping("/children/{childId}/words/{wordId}")
    public ResponseEntity<?> updateWord(
            @PathVariable Long childId,
            @PathVariable Long wordId,
            @RequestBody Word word,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        try {
            Word updatedWord = dataEntryService.updateWord(wordId, childId, userId, word);
            return ResponseEntity.ok(updatedWord);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error updating word", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error updating word"));
        }
    }

    @DeleteMapping("/children/{childId}/words/{wordId}")
    public ResponseEntity<?> deleteWord(
            @PathVariable Long childId,
            @PathVariable Long wordId,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        try {
            dataEntryService.deleteWord(wordId, childId, userId);
            return ResponseEntity.ok(Map.of("message", "Word deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error deleting word", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error deleting word"));
        }
    }

    // ========== PHRASE ENDPOINTS ==========

    @GetMapping("/children/{childId}/phrases")
    public ResponseEntity<?> getPhrases(@PathVariable Long childId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        try {
            List<Phrase> phrases = dataEntryService.getPhrases(childId, userId);
            return ResponseEntity.ok(phrases);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error fetching phrases", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching phrases"));
        }
    }

    @PostMapping("/children/{childId}/phrases")
    public ResponseEntity<?> addPhrase(
            @PathVariable Long childId,
            @RequestBody Phrase phrase,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        try {
            Phrase savedPhrase = dataEntryService.addPhrase(childId, userId, phrase);
            return ResponseEntity.ok(savedPhrase);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error adding phrase", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error adding phrase"));
        }
    }

    @PutMapping("/children/{childId}/phrases/{phraseId}")
    public ResponseEntity<?> updatePhrase(
            @PathVariable Long childId,
            @PathVariable Long phraseId,
            @RequestBody Phrase phrase,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        try {
            Phrase updatedPhrase = dataEntryService.updatePhrase(phraseId, childId, userId, phrase);
            return ResponseEntity.ok(updatedPhrase);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error updating phrase", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error updating phrase"));
        }
    }

    @DeleteMapping("/children/{childId}/phrases/{phraseId}")
    public ResponseEntity<?> deletePhrase(
            @PathVariable Long childId,
            @PathVariable Long phraseId,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        try {
            dataEntryService.deletePhrase(phraseId, childId, userId);
            return ResponseEntity.ok(Map.of("message", "Phrase deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error deleting phrase", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error deleting phrase"));
        }
    }

    // ========== SONG ENDPOINTS ==========

    @GetMapping("/children/{childId}/songs")
    public ResponseEntity<?> getSongs(@PathVariable Long childId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        try {
            List<Song> songs = dataEntryService.getSongs(childId, userId);
            return ResponseEntity.ok(songs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error fetching songs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching songs"));
        }
    }

    @PostMapping("/children/{childId}/songs")
    public ResponseEntity<?> addSong(
            @PathVariable Long childId,
            @RequestBody Song song,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        try {
            Song savedSong = dataEntryService.addSong(childId, userId, song);
            return ResponseEntity.ok(savedSong);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error adding song", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error adding song"));
        }
    }

    @PutMapping("/children/{childId}/songs/{songId}")
    public ResponseEntity<?> updateSong(
            @PathVariable Long childId,
            @PathVariable Long songId,
            @RequestBody Song song,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        try {
            Song updatedSong = dataEntryService.updateSong(songId, childId, userId, song);
            return ResponseEntity.ok(updatedSong);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error updating song", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error updating song"));
        }
    }

    @DeleteMapping("/children/{childId}/songs/{songId}")
    public ResponseEntity<?> deleteSong(
            @PathVariable Long childId,
            @PathVariable Long songId,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        try {
            dataEntryService.deleteSong(songId, childId, userId);
            return ResponseEntity.ok(Map.of("message", "Song deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error deleting song", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error deleting song"));
        }
    }

    // ========== LETTER ENDPOINTS ==========

    @GetMapping("/children/{childId}/letters")
    public ResponseEntity<?> getLetters(@PathVariable Long childId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        try {
            List<Letter> letters = dataEntryService.getLetters(childId, userId);
            return ResponseEntity.ok(letters);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error fetching letters", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching letters"));
        }
    }

    @PostMapping("/children/{childId}/letters")
    public ResponseEntity<?> addLetter(
            @PathVariable Long childId,
            @RequestBody Letter letter,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        try {
            Letter savedLetter = dataEntryService.addLetter(childId, userId, letter);
            return ResponseEntity.ok(savedLetter);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error adding letter", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error adding letter"));
        }
    }

    @PutMapping("/children/{childId}/letters/{letterId}")
    public ResponseEntity<?> updateLetter(
            @PathVariable Long childId,
            @PathVariable Long letterId,
            @RequestBody Letter letter,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        try {
            Letter updatedLetter = dataEntryService.updateLetter(letterId, childId, userId, letter);
            return ResponseEntity.ok(updatedLetter);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error updating letter", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error updating letter"));
        }
    }

    @DeleteMapping("/children/{childId}/letters/{letterId}")
    public ResponseEntity<?> deleteLetter(
            @PathVariable Long childId,
            @PathVariable Long letterId,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        try {
            dataEntryService.deleteLetter(letterId, childId, userId);
            return ResponseEntity.ok(Map.of("message", "Letter deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error deleting letter", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error deleting letter"));
        }
    }
}