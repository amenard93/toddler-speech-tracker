package com.toddlerspeechtracker.toddlerspeechtracker_gradle.service;

import com.toddlerspeechtracker.toddlerspeechtracker_gradle.model.*;
import com.toddlerspeechtracker.toddlerspeechtracker_gradle.repo.*;
import lombok.extern.log4j.Log4j2;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class GoogleSheetsService {

    private final WordRepository wordRepo;
    private final PhraseRepository phraseRepo;
    private final SongRepository songRepo;
    private final LetterRepository letterRepo;
    private final ChildRepository childRepo;
    private final UserRepository userRepo;


    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @Value("${sheets.spreadsheetId}")
    private String spreadsheetId;

    @Value("${sheets.credentialsFilePath}")
    private Resource credentialsResource;

    public GoogleSheetsService(WordRepository wordRepo,
                               PhraseRepository phraseRepo,
                               SongRepository songRepo,
                               LetterRepository letterRepo,
                               ChildRepository childRepo,
                               UserRepository userRepo) {
        log.info("In GoogleSheetsService");
        this.wordRepo = wordRepo;
        this.phraseRepo = phraseRepo;
        this.songRepo = songRepo;
        this.letterRepo = letterRepo;
        this.childRepo = childRepo;
        this.userRepo = userRepo;
    }

    private Sheets sheetsService() throws Exception {
        log.info("In GoogleSheetsService.sheetsService");

        InputStream in = credentialsResource.getInputStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                .createScoped(List.of("https://www.googleapis.com/auth/spreadsheets"));
        var transport = GoogleNetHttpTransport.newTrustedTransport();
        return new Sheets.Builder(transport, JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                .setApplicationName("toddler-speech-tracker")
                .build();
    }

    /**
     * Fetch data from Google Sheets without saving to database
     * NOTE: This method now requires a childId parameter to associate data properly
     */
    public SyncResult fetchDataOnly(Long childId) throws Exception {
        log.info("In GoogleSheetsService.fetchDataOnly - fetching without saving for childId: " + childId);

        // Verify child exists
        Child child = childRepo.findById(childId)
                .orElseThrow(() -> new RuntimeException("Child not found with id: " + childId));

        Sheets service = sheetsService();
        SyncResult result = new SyncResult();

        try {
            // Words sheet
            log.info("Fetching Words sheet...");
            ValueRange wordsValues = service.spreadsheets().values()
                    .get(spreadsheetId, "Words")
                    .execute();
            List<List<Object>> wordsRows = wordsValues.getValues() != null ? wordsValues.getValues() : new ArrayList<>();
            log.info("Words rows fetched: " + wordsRows.size());
            result.words = parseWords(wordsRows, child);

            // Phrases sheet
            log.info("Fetching Phrases sheet...");
            ValueRange phraseValues = service.spreadsheets().values()
                    .get(spreadsheetId, "Phrases")
                    .execute();
            List<List<Object>> phraseRows = phraseValues.getValues() != null ? phraseValues.getValues() : new ArrayList<>();
            log.info("Phrases rows fetched: " + phraseRows.size());
            result.phrases = parsePhrases(phraseRows, child);

            // Songs sheet
            log.info("Fetching Songs sheet...");
            ValueRange songValues = service.spreadsheets().values()
                    .get(spreadsheetId, "Songs")
                    .execute();
            List<List<Object>> songRows = songValues.getValues() != null ? songValues.getValues() : new ArrayList<>();
            log.info("Songs rows fetched: " + songRows.size());
            result.songs = parseSongs(songRows, child);

            // Letters sheet
            log.info("Fetching Letters sheet...");
            ValueRange letterValues = service.spreadsheets().values()
                    .get(spreadsheetId, "Letters")
                    .execute();
            List<List<Object>> letterRows = letterValues.getValues() != null ? letterValues.getValues() : new ArrayList<>();
            log.info("Letters rows fetched: " + letterRows.size());
            result.letters = parseLetters(letterRows, child);

            log.info("All sheets fetched successfully");
        } catch (Exception e) {
            log.error("Error fetching sheets data: " + e.getMessage(), e);
            throw e;
        }

        return result;
    }

    /**
     * Fetch data from Google Sheets and save to database
     */
    public SyncResult fetchAndSaveAll(Long childId) throws Exception {
        log.info("In GoogleSheetsService.fetchAndSaveAll for childId: " + childId);

        // Verify child exists
        Child child = childRepo.findById(childId)
                .orElseThrow(() -> new RuntimeException("Child not found with id: " + childId));

        Sheets service = sheetsService();
        SyncResult result = new SyncResult();

        // Words sheet
        ValueRange wordsValues = service.spreadsheets().values()
                .get(spreadsheetId, "Words")
                .execute();
        List<List<Object>> wordsRows = wordsValues.getValues() != null ? wordsValues.getValues() : new ArrayList<>();
        List<Word> parsedWords = parseWords(wordsRows, child);
        List<Word> savedWords = upsertWords(parsedWords);
        result.words = savedWords;

        // Phrases sheet
        ValueRange phraseValues = service.spreadsheets().values()
                .get(spreadsheetId, "Phrases")
                .execute();
        List<List<Object>> phraseRows = phraseValues.getValues() != null ? phraseValues.getValues() : new ArrayList<>();
        List<Phrase> parsedPhrases = parsePhrases(phraseRows, child);
        List<Phrase> savedPhrases = upsertPhrases(parsedPhrases);
        result.phrases = savedPhrases;

        // Songs sheet
        ValueRange songValues = service.spreadsheets().values()
                .get(spreadsheetId, "Songs")
                .execute();
        List<List<Object>> songRows = songValues.getValues() != null ? songValues.getValues() : new ArrayList<>();
        List<Song> parsedSongs = parseSongs(songRows, child);
        List<Song> savedSongs = upsertSongs(parsedSongs);
        result.songs = savedSongs;

        // Letters sheet
        ValueRange letterValues = service.spreadsheets().values()
                .get(spreadsheetId, "Letters")
                .execute();
        List<List<Object>> letterRows = letterValues.getValues() != null ? letterValues.getValues() : new ArrayList<>();
        List<Letter> parsedLetters = parseLetters(letterRows, child);
        List<Letter> savedLetters = upsertLetters(parsedLetters);
        result.letters = savedLetters;

        return result;
    }

    private List<Word> upsertWords(List<Word> newWords) {
        log.info("Upserting " + newWords.size() + " words");
        List<Word> result = new ArrayList<>();

        for (Word newWord : newWords) {
            if (newWord.getWord() == null || newWord.getWord().trim().isEmpty()) {
                log.warn("Skipping word with null/empty word field");
                continue;
            }

            Long childId = newWord.getChild().getChildId();
            Optional<Word> existingOpt = wordRepo.findByChild_ChildIdAndWord(childId, newWord.getWord());

            if (existingOpt.isPresent()) {
                Word existing = existingOpt.get();
                log.info("Updating existing word: " + existing.getWord() + " for child: " + childId);

                // Update all fields
                existing.setSigned(newWord.getSigned());
                existing.setSignedDate(newWord.getSignedDate());
                existing.setVerbal(newWord.getVerbal());
                existing.setVerbalDate(newWord.getVerbalDate());
                existing.setActualPronunciation(newWord.getActualPronunciation());
                existing.setNotes(newWord.getNotes());
                existing.setLearningSource(newWord.getLearningSource());

                result.add(wordRepo.save(existing));
            } else {
                log.info("Inserting new word: " + newWord.getWord() + " for child: " + childId);
                result.add(wordRepo.save(newWord));
            }
        }
        return result;
    }

    private List<Phrase> upsertPhrases(List<Phrase> newPhrases) {
        log.info("Upserting " + newPhrases.size() + " phrases");
        List<Phrase> result = new ArrayList<>();

        for (Phrase newPhrase : newPhrases) {
            if (newPhrase.getPhrase() == null || newPhrase.getPhrase().trim().isEmpty()) {
                log.warn("Skipping phrase with null/empty phrase field");
                continue;
            }

            Long childId = newPhrase.getChild().getChildId();
            Optional<Phrase> existingOpt = phraseRepo.findByChild_ChildIdAndPhrase(childId, newPhrase.getPhrase());

            if (existingOpt.isPresent()) {
                Phrase existing = existingOpt.get();
                log.info("Updating existing phrase: " + existing.getPhrase() + " for child: " + childId);

                existing.setDateSaid(newPhrase.getDateSaid());
                existing.setFunnyRating(newPhrase.getFunnyRating());
                existing.setCuteRating(newPhrase.getCuteRating());
                existing.setLearningSource(newPhrase.getLearningSource());
                existing.setNotes(newPhrase.getNotes());

                result.add(phraseRepo.save(existing));
            } else {
                log.info("Inserting new phrase: " + newPhrase.getPhrase() + " for child: " + childId);
                result.add(phraseRepo.save(newPhrase));
            }
        }
        return result;
    }

    private List<Song> upsertSongs(List<Song> newSongs) {
        log.info("Upserting " + newSongs.size() + " songs");
        List<Song> result = new ArrayList<>();

        for (Song newSong : newSongs) {
            if (newSong.getSongTitle() == null || newSong.getSongTitle().trim().isEmpty()) {
                log.warn("Skipping song with null/empty songTitle field");
                continue;
            }

            Long childId = newSong.getChild().getChildId();
            Optional<Song> existingOpt = songRepo.findByChild_ChildIdAndSongTitle(childId, newSong.getSongTitle());

            if (existingOpt.isPresent()) {
                Song existing = existingOpt.get();
                log.info("Updating existing song: " + existing.getSongTitle() + " for child: " + childId);

                existing.setDateFirstSang(newSong.getDateFirstSang());
                existing.setSource(newSong.getSource());
                existing.setNotes(newSong.getNotes());

                result.add(songRepo.save(existing));
            } else {
                log.info("Inserting new song: " + newSong.getSongTitle() + " for child: " + childId);
                result.add(songRepo.save(newSong));
            }
        }
        return result;
    }

    private List<Letter> upsertLetters(List<Letter> newLetters) {
        log.info("Upserting " + newLetters.size() + " letters");
        List<Letter> result = new ArrayList<>();

        for (Letter newLetter : newLetters) {
            if (newLetter.getLetters() == null || newLetter.getLetters().trim().isEmpty()) {
                log.warn("Skipping letter with null/empty letters field");
                continue;
            }

            Long childId = newLetter.getChild().getChildId();
            Optional<Letter> existingOpt = letterRepo.findByChild_ChildIdAndLetters(childId, newLetter.getLetters());

            if (existingOpt.isPresent()) {
                Letter existing = existingOpt.get();
                log.info("Updating existing letter: " + existing.getLetters() + " for child: " + childId);

                existing.setRecognized(newLetter.getRecognized());
                existing.setRecognizedDate(newLetter.getRecognizedDate());
                existing.setSoundItOut(newLetter.getSoundItOut());
                existing.setSoundItOutDate(newLetter.getSoundItOutDate());

                result.add(letterRepo.save(existing));
            } else {
                log.info("Inserting new letter: " + newLetter.getLetters() + " for child: " + childId);
                result.add(letterRepo.save(newLetter));
            }
        }
        return result;
    }

    // Parse methods - updated to handle new column structure and set Child relationship
    private List<Word> parseWords(List<List<Object>> rows, Child child) {
        log.info("In GoogleSheetsService.parseWords");

        List<Word> out = new ArrayList<>();
        if (rows.size() <= 1) return out;

        for (int i = 1; i < rows.size(); i++) {
            List<Object> r = rows.get(i);
            Word w = new Word();
            w.setChild(child);
            w.setWord(get(r, 0));
            w.setSigned(parseBoolean(get(r, 1)));
            w.setSignedDate(get(r, 2));
            w.setVerbal(parseBoolean(get(r, 3)));
            w.setVerbalDate(get(r, 4));
            w.setActualPronunciation(get(r, 5));
            w.setNotes(get(r, 6));
            w.setLearningSource(get(r, 7));
            out.add(w);
        }
        return out;
    }

    private List<Phrase> parsePhrases(List<List<Object>> rows, Child child) {
        log.info("In GoogleSheetsService.parsePhrases");

        List<Phrase> out = new ArrayList<>();
        if (rows.size() <= 1) return out;

        for (int i = 1; i < rows.size(); i++) {
            List<Object> r = rows.get(i);
            Phrase p = new Phrase();
            p.setChild(child);
            p.setPhrase(get(r, 0));
            p.setDateSaid(get(r, 1));
            p.setFunnyRating(get(r, 2));
            p.setCuteRating(get(r, 3));
            p.setLearningSource(get(r, 4));
            p.setNotes(get(r, 5));
            out.add(p);
        }
        return out;
    }

    private List<Song> parseSongs(List<List<Object>> rows, Child child) {
        log.info("In GoogleSheetsService.parseSongs");

        List<Song> out = new ArrayList<>();
        if (rows.size() <= 1) return out;

        for (int i = 1; i < rows.size(); i++) {
            List<Object> r = rows.get(i);
            Song s = new Song();
            s.setChild(child);
            s.setSongTitle(get(r, 0));
            s.setDateFirstSang(get(r, 1));
            s.setSource(get(r, 2));
            s.setNotes(get(r, 3));
            out.add(s);
        }
        return out;
    }

    private List<Letter> parseLetters(List<List<Object>> rows, Child child) {
        log.info("In GoogleSheetsService.parseLetters");

        List<Letter> out = new ArrayList<>();
        if (rows.size() <= 1) return out;

        for (int i = 1; i < rows.size(); i++) {
            List<Object> r = rows.get(i);
            Letter l = new Letter();
            l.setChild(child);
            l.setLetters(get(r, 0));
            l.setRecognized(get(r, 1));
            l.setRecognizedDate(get(r, 2));
            l.setSoundItOut(get(r, 3));
            l.setSoundItOutDate(get(r, 4));
            out.add(l);
        }
        return out;
    }

    private String get(List<Object> r, int idx) {
        if (idx >= r.size()) return null;
        Object o = r.get(idx);
        return o == null ? null : o.toString().trim();
    }

    /**
     * Helper method to parse boolean values from Google Sheets
     * Accepts: "true", "TRUE", "yes", "YES", "Y", "1" as true
     * Everything else (including null/empty) as false
     */
    private Boolean parseBoolean(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        String normalized = value.trim().toUpperCase();
        return normalized.equals("TRUE") ||
                normalized.equals("YES") ||
                normalized.equals("Y") ||
                normalized.equals("1");
    }

    public static class SyncResult {
        public List<Word> words = new ArrayList<>();
        public List<Phrase> phrases = new ArrayList<>();
        public List<Song> songs = new ArrayList<>();
        public List<Letter> letters = new ArrayList<>();
    }

    public String testConnection() throws Exception {
        log.info("Testing Google Sheets API connection");

        Sheets service = sheetsService();

        // Get spreadsheet metadata (doesn't require reading sheets)
        var spreadsheet = service.spreadsheets().get(spreadsheetId).execute();

        StringBuilder info = new StringBuilder();
        info.append("Connection successful!\n");
        info.append("Spreadsheet title: ").append(spreadsheet.getProperties().getTitle()).append("\n");
        info.append("Sheet tabs found: ").append(spreadsheet.getSheets().size()).append("\n");

        spreadsheet.getSheets().forEach(sheet -> {
            info.append("  - ").append(sheet.getProperties().getTitle()).append("\n");
        });

        return info.toString();
    }
}