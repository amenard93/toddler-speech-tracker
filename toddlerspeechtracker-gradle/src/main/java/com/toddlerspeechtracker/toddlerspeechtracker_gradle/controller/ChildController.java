package com.toddlerspeechtracker.toddlerspeechtracker_gradle.controller;

import com.toddlerspeechtracker.toddlerspeechtracker_gradle.model.Child;
import com.toddlerspeechtracker.toddlerspeechtracker_gradle.service.ChildService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/children")
public class ChildController {

    private final ChildService childService;

    public ChildController(ChildService childService) {
        this.childService = childService;
    }

    @GetMapping
    public ResponseEntity<?> getChildren(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        try {
            List<Child> children = childService.getChildrenByUserId(userId);
            return ResponseEntity.ok(children);
        } catch (Exception e) {
            log.error("Error fetching children", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching children"));
        }
    }

    @GetMapping("/{childId}")
    public ResponseEntity<?> getChild(@PathVariable Long childId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        try {
            Child child = childService.getChild(childId, userId);
            return ResponseEntity.ok(child);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error fetching child", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching child"));
        }
    }

    @PostMapping
    public ResponseEntity<?> addChild(@RequestBody AddChildRequest request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        log.info("Adding child for user: " + userId);

        try {
            Child child = childService.addChild(
                    userId,
                    request.getChildName(),
                    request.getBirthDate()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Child added successfully");
            response.put("child", child);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error adding child", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error adding child"));
        }
    }

    @PutMapping("/{childId}")
    public ResponseEntity<?> updateChild(
            @PathVariable Long childId,
            @RequestBody UpdateChildRequest request,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        try {
            Child child = childService.updateChild(
                    childId,
                    userId,
                    request.getChildName(),
                    request.getBirthDate()
            );

            return ResponseEntity.ok(child);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error updating child", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error updating child"));
        }
    }

    @DeleteMapping("/{childId}")
    public ResponseEntity<?> deleteChild(@PathVariable Long childId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        try {
            childService.deleteChild(childId, userId);
            return ResponseEntity.ok(Map.of("message", "Child deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error deleting child", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error deleting child"));
        }
    }

    // Request DTOs
    public static class AddChildRequest {
        private String childName;
        private LocalDate birthDate;

        public String getChildName() { return childName; }
        public void setChildName(String childName) { this.childName = childName; }
        public LocalDate getBirthDate() { return birthDate; }
        public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    }

    public static class UpdateChildRequest {
        private String childName;
        private LocalDate birthDate;

        public String getChildName() { return childName; }
        public void setChildName(String childName) { this.childName = childName; }
        public LocalDate getBirthDate() { return birthDate; }
        public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    }
}