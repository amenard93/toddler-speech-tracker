package com.toddlerspeechtracker.toddlerspeechtracker_gradle.repo;

import com.toddlerspeechtracker.toddlerspeechtracker_gradle.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findByChild_ChildIdAndSongTitle(Long childId, String songTitle);
    List<Song> findByChild_ChildId(Long childId);
}