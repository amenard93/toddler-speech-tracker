package com.toddlerspeechtracker.toddlerspeechtracker_gradle.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "word")
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id")
    private Long wordId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    @Column(length = 255)
    private String word;

    @Column(nullable = false)
    private Boolean signed = false;

    @Column(name = "signed_date", length = 50)
    private String signedDate;

    @Column(nullable = false)
    private Boolean verbal = false;

    @Column(name = "verbal_date", length = 50)
    private String verbalDate;

    @Column(name = "actual_pronunciation", length = 1024)
    private String actualPronunciation;

    @Column(length = 2048)
    private String notes;

    @Column(name = "learning_source", length = 255)
    private String learningSource;

    @Column(name = "created_timestamp", nullable = false, updatable = false)
    private LocalDateTime createdTimestamp;

    @Column(name = "updated_timestamp", nullable = false)
    private LocalDateTime updatedTimestamp;

    @PrePersist
    protected void onCreate() {
        createdTimestamp = LocalDateTime.now();
        updatedTimestamp = LocalDateTime.now();
        if (signed == null) signed = false;
        if (verbal == null) verbal = false;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTimestamp = LocalDateTime.now();
    }
}