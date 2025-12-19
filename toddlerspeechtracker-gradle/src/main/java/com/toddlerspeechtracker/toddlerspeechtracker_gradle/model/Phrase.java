package com.toddlerspeechtracker.toddlerspeechtracker_gradle.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "phrase")
public class Phrase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phrase_id")
    private Long phraseId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    @Column(length = 1024)
    private String phrase;

    @Column(name = "date_said", length = 50)
    private String dateSaid;

    @Column(name = "funny_rating", length = 50)
    private String funnyRating;

    @Column(name = "cute_rating", length = 50)
    private String cuteRating;

    @Column(name = "learning_source", length = 255)
    private String learningSource;

    @Column(length = 2048)
    private String notes;

    @Column(name = "created_timestamp", nullable = false, updatable = false)
    private LocalDateTime createdTimestamp;

    @Column(name = "updated_timestamp", nullable = false)
    private LocalDateTime updatedTimestamp;

    @PrePersist
    protected void onCreate() {
        createdTimestamp = LocalDateTime.now();
        updatedTimestamp = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTimestamp = LocalDateTime.now();
    }
}