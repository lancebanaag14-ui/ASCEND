package com.brewcoders.ascend.backend.model.mission;

import java.time.LocalDate;

/* Purpose: Represent a study mission using the shared mission rules.
 */
public class StudyMission extends ParentMission {

    public StudyMission(String name, String notes, LocalDate dueDate, String difficulty) {
        super(name, notes, dueDate, "Study", difficulty);
    }

    @Override
    public String getCategory() { return "Study"; }
}
