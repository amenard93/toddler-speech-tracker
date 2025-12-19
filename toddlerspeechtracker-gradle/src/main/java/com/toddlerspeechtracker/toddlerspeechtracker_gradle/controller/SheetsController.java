package com.toddlerspeechtracker.toddlerspeechtracker_gradle.controller;

import lombok.extern.log4j.Log4j2;
import com.toddlerspeechtracker.toddlerspeechtracker_gradle.service.GoogleSheetsService;
import com.toddlerspeechtracker.toddlerspeechtracker_gradle.service.GoogleSheetsService.SyncResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
public class SheetsController {

    private final GoogleSheetsService sheetsService;

    @Value("${sheets.defaultChildId:1}")
    private Long defaultChildId;

    public SheetsController(GoogleSheetsService sheetsService) {
        this.sheetsService = sheetsService;
    }

    @PostMapping("/api/fetch")
    @ResponseBody
    public ResponseEntity<?> fetchSheets() {
        log.info("In SheetsController.fetchSheets for default childId: " + defaultChildId);

        try {
            SyncResult result = sheetsService.fetchDataOnly(defaultChildId);
            log.info("Successfully fetched data - Words: " + result.words.size() +
                    ", Phrases: " + result.phrases.size() +
                    ", Songs: " + result.songs.size() +
                    ", Letters: " + result.letters.size());
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            log.error("Error fetching sheets", ex);
            return ResponseEntity.status(500).body("Error fetching sheets: " + ex.getMessage());
        }
    }

    @PostMapping("/api/sync")
    @ResponseBody
    public ResponseEntity<?> syncSheets() {
        log.info("In SheetsController.syncSheets for default childId: " + defaultChildId);

        try {
            SyncResult result = sheetsService.fetchAndSaveAll(defaultChildId);
            log.info("Successfully synced data - Words: " + result.words.size() +
                    ", Phrases: " + result.phrases.size() +
                    ", Songs: " + result.songs.size() +
                    ", Letters: " + result.letters.size());
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            log.error("Error syncing sheets", ex);
            return ResponseEntity.status(500).body("Error syncing sheets: " + ex.getMessage());
        }
    }

    @GetMapping("/api/test-connection")
    @ResponseBody
    public ResponseEntity<?> testConnection() {
        log.info("In SheetsController.testConnection");

        try {
            String result = sheetsService.testConnection();
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            log.error("Error testing connection", ex);
            return ResponseEntity.status(500).body("Error: " + ex.getMessage());
        }
    }
}