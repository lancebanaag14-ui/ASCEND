package com.brewcoders.ascend.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Mission {
    private String name;
    private String description;
    private LocalDate dueDate;
    private String category;
    private String difficulty;
    private int points;
    private String status; // "ACTIVE", "COMPLETED", "FAILED"

    public Mission(String name, String description, LocalDate dueDate, String category, String difficulty, int points) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.category = category;
        this.difficulty = difficulty;
        this.points = points;
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
    public void setStatus(String status) { this.status = status; }

    public boolean isCompleted() { return "COMPLETED".equalsIgnoreCase(status); }
    public boolean isFailed() { return "FAILED".equalsIgnoreCase(status); }

    // Formatted Date Helper
    public String getFormattedDueDate() {
        if (dueDate == null) return "";
        return dueDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
    }
}