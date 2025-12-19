package com.toddlerspeechtracker.toddlerspeechtracker_gradle.service;

import com.toddlerspeechtracker.toddlerspeechtracker_gradle.model.*;
import com.toddlerspeechtracker.toddlerspeechtracker_gradle.repo.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
public class DataEntryService {

    private final WordRepository wordRepo;
    private final PhraseRepository phraseRepo;
    private final SongRepository songRepo;
    private final LetterRepository letterRepo;
    private final ChildRepository childRepo;

    public DataEntryService(
            WordRepository wordRepo,
            PhraseRepository phraseRepo,
            SongRepository songRepo,
            LetterRepository letterRepo,
            ChildRepository childRepo) {
        this.wordRepo = wordRepo;
        this.phraseRepo = phraseRepo;
        this.songRepo = songRepo;
        this.letterRepo = letterRepo;
        this.childRepo = childRepo;
    }

    private Child verifyChildAccess(Long childId, Long userId) {
        Child child = childRepo.findById(childId)
                .orElseThrow(() -> new IllegalArgumentException("Child not found"));

        if (!child.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("Access denied: This child does not belong to you");
        }

        return child;
    }

    // ========== WORD OPERATIONS ==========

    public List<Word> getWords(Long childId, Long userId) {
        verifyChildAccess(childId, userId);
        return wordRepo.findByChild_ChildId(childId);
    }

    @Transactional
    public Word addWord(Long childId, Long userId, Word word) {
        Child child = verifyChildAccess(childId, userId);

        if (word.getWord() == null || word.getWord().trim().isEmpty()) {
            throw new IllegalArgumentException("Word is required");
        }

        word.setChild(child);
        word.setWordId(null); // Ensure it's a new record

        return wordRepo.save(word);
    }

    @Transactional
    public Word updateWord(Long wordId, Long childId, Long userId, Word updatedWord) {
        verifyChildAccess(childId, userId);

        Word existingWord = wordRepo.findById(wordId)
                .orElseThrow(() -> new IllegalArgumentException("Word not found"));

        if (!existingWord.getChild().getChildId().equals(childId)) {
            throw new IllegalArgumentException("Word does not belong to this child");
        }

        // Update fields
        if (updatedWord.getWord() != null) {
            existingWord.setWord(updatedWord.getWord());
        }
        existingWord.setSigned(updatedWord.getSigned());
        existingWord.setSignedDate(updatedWord.getSignedDate());
        existingWord.setVerbal(updatedWord.getVerbal());
        existingWord.setVerbalDate(updatedWord.getVerbalDate());
        existingWord.setActualPronunciation(updatedWord.getActualPronunciation());
        existingWord.setNotes(updatedWord.getNotes());
        existingWord.setLearningSource(updatedWord.getLearningSource());

        return wordRepo.save(existingWord);
    }

    @Transactional
    public void deleteWord(Long wordId, Long childId, Long userId) {
        verifyChildAccess(childId, userId);

        Word word = wordRepo.findById(wordId)
                .orElseThrow(() -> new IllegalArgumentException("Word not found"));

        if (!word.getChild().getChildId().equals(childId)) {
            throw new IllegalArgumentException("Word does not belong to this child");
        }

        wordRepo.delete(word);
    }

    // ========== PHRASE OPERATIONS ==========

    public List<Phrase> getPhrases(Long childId, Long userId) {
        verifyChildAccess(childId, userId);
        return phraseRepo.findByChild_ChildId(childId);
    }

    @Transactional
    public Phrase addPhrase(Long childId, Long userId, Phrase phrase) {
        Child child = verifyChildAccess(childId, userId);

        if (phrase.getPhrase() == null || phrase.getPhrase().trim().isEmpty()) {
            throw new IllegalArgumentException("Phrase is required");
        }

        phrase.setChild(child);
        phrase.setPhraseId(null);

        return phraseRepo.save(phrase);
    }

    @Transactional
    public Phrase updatePhrase(Long phraseId, Long childId, Long userId, Phrase updatedPhrase) {
        verifyChildAccess(childId, userId);

        Phrase existingPhrase = phraseRepo.findById(phraseId)
                .orElseThrow(() -> new IllegalArgumentException("Phrase not found"));

        if (!existingPhrase.getChild().getChildId().equals(childId)) {
            throw new IllegalArgumentException("Phrase does not belong to this child");
        }

        if (updatedPhrase.getPhrase() != null) {
            existingPhrase.setPhrase(updatedPhrase.getPhrase());
        }
        existingPhrase.setDateSaid(updatedPhrase.getDateSaid());
        existingPhrase.setFunnyRating(updatedPhrase.getFunnyRating());
        existingPhrase.setCuteRating(updatedPhrase.getCuteRating());
        existingPhrase.setLearningSource(updatedPhrase.getLearningSource());
        existingPhrase.setNotes(updatedPhrase.getNotes());

        return phraseRepo.save(existingPhrase);
    }

    @Transactional
    public void deletePhrase(Long phraseId, Long childId, Long userId) {
        verifyChildAccess(childId, userId);

        Phrase phrase = phraseRepo.findById(phraseId)
                .orElseThrow(() -> new IllegalArgumentException("Phrase not found"));

        if (!phrase.getChild().getChildId().equals(childId)) {
            throw new IllegalArgumentException("Phrase does not belong to this child");
        }

        phraseRepo.delete(phrase);
    }

    // ========== SONG OPERATIONS ==========

    public List<Song> getSongs(Long childId, Long userId) {
        verifyChildAccess(childId, userId);
        return songRepo.findByChild_ChildId(childId);
    }

    @Transactional
    public Song addSong(Long childId, Long userId, Song song) {
        Child child = verifyChildAccess(childId, userId);

        if (song.getSongTitle() == null || song.getSongTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Song title is required");
        }

        song.setChild(child);
        song.setSongId(null);

        return songRepo.save(song);
    }

    @Transactional
    public Song updateSong(Long songId, Long childId, Long userId, Song updatedSong) {
        verifyChildAccess(childId, userId);

        Song existingSong = songRepo.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Song not found"));

        if (!existingSong.getChild().getChildId().equals(childId)) {
            throw new IllegalArgumentException("Song does not belong to this child");
        }

        if (updatedSong.getSongTitle() != null) {
            existingSong.setSongTitle(updatedSong.getSongTitle());
        }
        existingSong.setDateFirstSang(updatedSong.getDateFirstSang());
        existingSong.setSource(updatedSong.getSource());
        existingSong.setNotes(updatedSong.getNotes());

        return songRepo.save(existingSong);
    }

    @Transactional
    public void deleteSong(Long songId, Long childId, Long userId) {
        verifyChildAccess(childId, userId);

        Song song = songRepo.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Song not found"));

        if (!song.getChild().getChildId().equals(childId)) {
            throw new IllegalArgumentException("Song does not belong to this child");
        }

        songRepo.delete(song);
    }

    // ========== LETTER OPERATIONS ==========

    public List<Letter> getLetters(Long childId, Long userId) {
        verifyChildAccess(childId, userId);
        return letterRepo.findByChild_ChildId(childId);
    }

    @Transactional
    public Letter addLetter(Long childId, Long userId, Letter letter) {
        Child child = verifyChildAccess(childId, userId);

        if (letter.getLetters() == null || letter.getLetters().trim().isEmpty()) {
            throw new IllegalArgumentException("Letter(s) is required");
        }

        letter.setChild(child);
        letter.setLetterId(null);

        return letterRepo.save(letter);
    }

    @Transactional
    public Letter updateLetter(Long letterId, Long childId, Long userId, Letter updatedLetter) {
        verifyChildAccess(childId, userId);

        Letter existingLetter = letterRepo.findById(letterId)
                .orElseThrow(() -> new IllegalArgumentException("Letter not found"));

        if (!existingLetter.getChild().getChildId().equals(childId)) {
            throw new IllegalArgumentException("Letter does not belong to this child");
        }

        if (updatedLetter.getLetters() != null) {
            existingLetter.setLetters(updatedLetter.getLetters());
        }
        existingLetter.setRecognized(updatedLetter.getRecognized());
        existingLetter.setRecognizedDate(updatedLetter.getRecognizedDate());
        existingLetter.setSoundItOut(updatedLetter.getSoundItOut());
        existingLetter.setSoundItOutDate(updatedLetter.getSoundItOutDate());

        return letterRepo.save(existingLetter);
    }

    @Transactional
    public void deleteLetter(Long letterId, Long childId, Long userId) {
        verifyChildAccess(childId, userId);

        Letter letter = letterRepo.findById(letterId)
                .orElseThrow(() -> new IllegalArgumentException("Letter not found"));

        if (!letter.getChild().getChildId().equals(childId)) {
            throw new IllegalArgumentException("Letter does not belong to this child");
        }

        letterRepo.delete(letter);
    }
}