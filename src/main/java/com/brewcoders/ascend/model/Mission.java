package com.brewcoders.ascend.model;

import java.time.LocalDate;

public class Mission {
    private String name;
    private String description;
    private LocalDate dueDate;
    private String category;
    private String difficulty;
    private int points;

    public Mission(String name, String description, LocalDate dueDate, String category, String difficulty, int points) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.category = category;
        this.difficulty = difficulty;
        this.points = points;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDate getDueDate() { return dueDate; }
    public String getCategory() { return category; }
    public String getDifficulty() { return difficulty; }
    public int getPoints() { return points; }
}