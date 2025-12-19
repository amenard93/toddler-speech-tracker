package com.toddlerspeechtracker.toddlerspeechtracker_gradle.service;

import com.toddlerspeechtracker.toddlerspeechtracker_gradle.model.Child;
import com.toddlerspeechtracker.toddlerspeechtracker_gradle.model.User;
import com.toddlerspeechtracker.toddlerspeechtracker_gradle.repo.ChildRepository;
import com.toddlerspeechtracker.toddlerspeechtracker_gradle.repo.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@Service
public class ChildService {

    private final ChildRepository childRepo;
    private final UserRepository userRepo;

    public ChildService(ChildRepository childRepo, UserRepository userRepo) {
        this.childRepo = childRepo;
        this.userRepo = userRepo;
    }

    public List<Child> getChildrenByUserId(Long userId) {
        log.info("Fetching children for user: " + userId);
        return childRepo.findByUser_UserId(userId);
    }

    public Child getChild(Long childId, Long userId) {
        log.info("Fetching child: " + childId + " for user: " + userId);

        Child child = childRepo.findById(childId)
                .orElseThrow(() -> new IllegalArgumentException("Child not found"));

        // Verify the child belongs to the user
        if (!child.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("Access denied: This child does not belong to you");
        }

        return child;
    }

    @Transactional
    public Child addChild(Long userId, String childName, LocalDate birthDate) {
        log.info("Adding child for user: " + userId);

        if (childName == null || childName.trim().isEmpty()) {
            throw new IllegalArgumentException("Child name is required");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Child child = new Child();
        child.setUser(user);
        child.setChildName(childName);
        child.setBirthDate(birthDate);

        Child savedChild = childRepo.save(child);
        log.info("Child added successfully: " + savedChild.getChildName());

        return savedChild;
    }

    @Transactional
    public Child updateChild(Long childId, Long userId, String childName, LocalDate birthDate) {
        log.info("Updating child: " + childId);

        Child child = getChild(childId, userId); // This verifies ownership

        if (childName != null && !childName.trim().isEmpty()) {
            child.setChildName(childName);
        }

        if (birthDate != null) {
            child.setBirthDate(birthDate);
        }

        Child updatedChild = childRepo.save(child);
        log.info("Child updated successfully: " + updatedChild.getChildName());

        return updatedChild;
    }

    @Transactional
    public void deleteChild(Long childId, Long userId) {
        log.info("Deleting child: " + childId);

        Child child = getChild(childId, userId); // This verifies ownership

        childRepo.delete(child);
        log.info("Child deleted successfully: " + childId);
    }
}