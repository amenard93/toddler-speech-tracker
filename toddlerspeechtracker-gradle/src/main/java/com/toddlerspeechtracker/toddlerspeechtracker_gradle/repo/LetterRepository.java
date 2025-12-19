package com.toddlerspeechtracker.toddlerspeechtracker_gradle.repo;

import com.toddlerspeechtracker.toddlerspeechtracker_gradle.model.Letter;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LetterRepository extends JpaRepository<Letter, Long> {
    Optional<Letter> findByChild_ChildIdAndLetters(Long childId, String letters);
    List<Letter> findByChild_ChildId(Long childId);
}