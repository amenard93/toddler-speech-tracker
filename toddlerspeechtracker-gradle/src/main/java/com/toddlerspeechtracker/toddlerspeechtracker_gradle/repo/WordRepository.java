package com.toddlerspeechtracker.toddlerspeechtracker_gradle.repo;

import com.toddlerspeechtracker.toddlerspeechtracker_gradle.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {
    Optional<Word> findByChild_ChildIdAndWord(Long childId, String word);
    List<Word> findByChild_ChildId(Long childId);
}