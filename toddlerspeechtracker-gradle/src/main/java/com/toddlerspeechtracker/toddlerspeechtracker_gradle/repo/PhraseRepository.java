package com.toddlerspeechtracker.toddlerspeechtracker_gradle.repo;

import com.toddlerspeechtracker.toddlerspeechtracker_gradle.model.Phrase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PhraseRepository extends JpaRepository<Phrase, Long> {
    Optional<Phrase> findByChild_ChildIdAndPhrase(Long childId, String phrase);
    List<Phrase> findByChild_ChildId(Long childId);
}