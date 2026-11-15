package com.brewcoders.ascend.backend.model.mission;

import java.time.LocalDate;
import com.brewcoders.ascend.model.Mission;
import com.brewcoders.ascend.backend.service.MissionValidation;

/* Purpose: Define the category format for missions.
 */
public abstract class ParentMission extends Mission {
    /* Purpose: Reuse the original mission validation and reward calculation.
     */
    protected ParentMission(String name, String notes, LocalDate dueDate, String category, String difficulty) {
        super(name, notes, dueDate, category, difficulty, MissionValidation.points(difficulty));
    }

    @Override
    public abstract String getCategory();
}
