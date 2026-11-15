package com.brewcoders.ascend.backend.service;

import java.time.LocalDate;
import com.brewcoders.ascend.model.Mission;
import com.brewcoders.ascend.backend.model.mission.*;

/* 	Creates a Study, Build, or Practice mission from the selected category.
 */
public class MissionFactory {

    private MissionFactory() {}

    /* Purpose: Construct one validated specialized mission without storing it twice.
     */
    public static Mission create(String name, String notes, LocalDate dueDate, String category, String difficulty) {
        String selected = MissionValidation.category(category);
        if (selected.equals("Practice")) return new PracticeMission(name, notes, dueDate, difficulty);
        if (selected.equals("Build")) return new BuildMission(name, notes, dueDate, difficulty);
        return new StudyMission(name, notes, dueDate, difficulty);
    }
}
