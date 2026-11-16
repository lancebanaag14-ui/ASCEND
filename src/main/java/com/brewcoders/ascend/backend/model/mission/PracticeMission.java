package com.brewcoders.ascend.backend.model.mission;

import java.time.LocalDate;

/* Purpose: Represent the mission using the shared mission rules.
 */
public class PracticeMission extends ParentMission {

    public PracticeMission(String name, String notes, LocalDate dueDate, String difficulty) {
        super(name, notes, dueDate, "Practice", difficulty);
    }
    
    @Override
    public String getCategory() { return "Practice"; }
}
