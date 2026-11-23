package com.brewcoders.ascend.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.brewcoders.ascend.backend.service.MissionValidation;
import com.brewcoders.ascend.backend.service.MissionLifecycle;

public class Mission {
    private String name;
    private String description;
    private LocalDate dueDate;
    private String category;
    private String difficulty;
    private int points;
    private String status; 

    public Mission(String name, String description, LocalDate dueDate, String category, String difficulty, int points) {
        // Purpose: Validate the existing constructor values before assigning the private fields.
        MissionValidation.validate(name, description, dueDate, category, difficulty);
        this.name = name.trim();
        this.description = description == null ? "" : description;
        this.dueDate = dueDate;
        // Purpose: Normalize valid choices and derive the correct reward in the backend.
        this.category = MissionValidation.category(category);
        this.difficulty = MissionValidation.difficulty(difficulty);
        this.points = MissionValidation.points(difficulty);
        this.status = "ACTIVE";
    }

    // Existing Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDate getDueDate() { return dueDate; }
    public String getCategory() { return category; }
    public String getDifficulty() { return difficulty; }
    public int getPoints() { return points; }

    // Title alias to support view components using getTitle()
    public String getTitle() { return name; }

    // Status Tracking Getters and Setters
    public String getStatus() { return status; }
   
    // Purpose: Keep the original setter while rejecting unknown or reversed final states.
    public void setStatus(String status) {
        MissionLifecycle.validateTransition(this, status);
        this.status = status;
    }

    public boolean isCompleted() { return "COMPLETED".equalsIgnoreCase(status); }
    public boolean isFailed() { return "FAILED".equalsIgnoreCase(status); }

    // Formatted Date Helper
    public String getFormattedDueDate() {
        if (dueDate == null) return "";
        return dueDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
    }
}