package com.brewcoders.ascend.backend.model.mission;

import java.time.LocalDate;

/* Purpose: Represent a build mission using the shared mission rules.
 */
public class BuildMission extends ParentMission {

    public BuildMission(String name, String notes, LocalDate dueDate, String difficulty) {
        super(name, notes, dueDate, "Build", difficulty);
    }

    @Override
    public String getCategory() { return "Build"; }
}
