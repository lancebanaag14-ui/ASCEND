package com.brewcoders.ascend.backend.service;

import java.time.LocalDate;

/* 	Checks mission inputs and calculates the correct reward from difficulty.
 */
public class MissionValidation {
    private MissionValidation() {}

    // Purpose: Check if the mission inputs are valid and throw an exception if not.
    public static void validate(String name, String notes, LocalDate due, String category, String difficulty) {
        if (name == null || name.trim().isEmpty() || name.trim().length() > 50) {
            throw new IllegalArgumentException("Mission name must contain 1-50 characters.");
        }
        if (notes != null && notes.length() > 62) {
            throw new IllegalArgumentException("Notes must contain at most 62 characters.");
        }
        if (due == null) throw new IllegalArgumentException("Choose a mission due date.");
        category(category);
        difficulty(difficulty);
    }

    // Purpose: Return the correct category format for missions.
    public static String category(String category) {
        if (category != null) {
            if (category.trim().equalsIgnoreCase("Study")) return "Study";
            if (category.trim().equalsIgnoreCase("Practice")) return "Practice";
            if (category.trim().equalsIgnoreCase("Build")) return "Build";
        }
        throw new IllegalArgumentException("Choose Study, Practice or Build.");
    }

    // Purpose: Return the correct difficulty format for missions.
    public static String difficulty(String difficulty) {
        if (difficulty != null) {
            if (difficulty.trim().equalsIgnoreCase("Easy")) return "Easy";
            if (difficulty.trim().equalsIgnoreCase("Medium")) return "Medium";
            if (difficulty.trim().equalsIgnoreCase("Hard")) return "Hard";
        }
        throw new IllegalArgumentException("Choose Easy, Medium or Hard.");
    }

    // Purpose: Return the correct points for the mission difficulty.
    public static int points(String difficulty) {
        String value = difficulty(difficulty);
        if (value.equals("Easy")) return 20;
        if (value.equals("Medium")) return 50;
        return 70;
    }
}
