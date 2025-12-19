package com.toddlerspeechtracker.toddlerspeechtracker_gradle.repo;

import com.toddlerspeechtracker.toddlerspeechtracker_gradle.model.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Long> {
    List<Child> findByUser_UserId(Long userId);
    Optional<Child> findByChildIdAndUser_UserId(Long childId, Long userId);
}