package com.brewcoders.ascend.controller;

import com.brewcoders.ascend.model.Mission;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MissionManager {
    private static MissionManager instance;
    private final List<Mission> missions = new ArrayList<>();
    private String currentUsername = "Guest User";

    private MissionManager() {}

    public static synchronized MissionManager getInstance() {
        if (instance == null) {
            instance = new MissionManager();
        }
        return instance;
    }

    // --- USERNAME CONTROLS ---

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String username) {
        if (username != null && !username.trim().isEmpty()) {
            this.currentUsername = username.trim();
        }
    }

    // --- FRONTEND SESSION & USER CONTROLS ---

    /**
     * Clears all in-memory missions for a course cycle while preserving the active username.
     * Call this when starting or resetting a course.
     */
    public void clearMissions() {
        this.missions.clear();
    }

    /**
     * Full logout reset - clears missions AND resets user session identity to default fallback.
     * Call this when logging out or deleting the account.
     */
    public void logout() {
        this.missions.clear();
        this.currentUsername = "Guest User";
    }

    /**
     * Replaces current in-memory missions.
     * Ready for backend integration later when loading saved user history on login.
     */
    public void setMissions(List<Mission> userMissions) {
        this.missions.clear();
        if (userMissions != null) {
            this.missions.addAll(userMissions);
        }
    }

    // --- MISSION OPERATIONS ---

    public void addMission(Mission mission) {
        missions.add(mission);
    }

    public List<Mission> getAllMissions() {
        return new ArrayList<>(missions);
    }

    public List<Mission> getActiveMissions() {
        LocalDate today = LocalDate.now();
        // Automatically mark active missions whose due dates have passed as FAILED
        for (Mission m : missions) {
            if ("ACTIVE".equalsIgnoreCase(m.getStatus()) && m.getDueDate() != null && m.getDueDate().isBefore(today)) {
                m.setStatus("FAILED");
            }
        }
        return missions.stream()
                .filter(m -> "ACTIVE".equalsIgnoreCase(m.getStatus()))
                .collect(Collectors.toList());
    }

    public List<Mission> getHistoryMissions() {
        // Returns all COMPLETED or FAILED missions for the History tab
        return missions.stream()
                .filter(m -> "COMPLETED".equalsIgnoreCase(m.getStatus()) || "FAILED".equalsIgnoreCase(m.getStatus()))
                .collect(Collectors.toList());
    }

    /**
     * Sums up points earned from all completed missions.
     */
    public int getTotalPoints() {
        return missions.stream()
                .filter(m -> "COMPLETED".equalsIgnoreCase(m.getStatus()))
                .mapToInt(Mission::getPoints)
                .sum();
    }

    public void completeMission(Mission mission) {
        mission.setStatus("COMPLETED");
    }

    public void failMission(Mission mission) {
        mission.setStatus("FAILED");
    }
}