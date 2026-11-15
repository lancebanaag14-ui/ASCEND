package com.brewcoders.ascend.backend.service;

import java.time.LocalDate;
import java.util.List;
import com.brewcoders.ascend.backend.model.CourseCycle;
import com.brewcoders.ascend.model.Mission;

/* 	Checks mission deadlines and course dates before allowing mission actions.
 */
public class MissionRules {
    private MissionRules() {}

    public static void validateNewMission(Mission mission, CourseCycle course, LocalDate today) {
        if (course == null) throw new IllegalStateException("Create a course first.");
        if (today == null) throw new IllegalArgumentException("A date is required.");
        if (!course.isActive(today)) {
            throw new IllegalStateException("Missions can be created only during the course dates.");
        }
        if (mission == null) throw new IllegalArgumentException("A mission is required.");
        if (!"ACTIVE".equals(mission.getStatus())) {
            throw new IllegalArgumentException("Only a new active mission can be added.");
        }
        if (mission.getDueDate().isBefore(today)) {
            throw new IllegalArgumentException("A new mission cannot be due in the past.");
        }
        if (mission.getDueDate().isBefore(course.getStartDate())
                || mission.getDueDate().isAfter(course.getEndDate())) {
            throw new IllegalArgumentException("The due date must be within your course dates.");
        }
    }

    public static boolean refresh(List<Mission> missions, CourseCycle course, LocalDate today) {
        if (today == null) throw new IllegalArgumentException("A date is required.");
        boolean changed = false;
    
        for (int i = 0; i < missions.size(); i++) {
            Mission mission = missions.get(i);
            String previousStatus = mission.getStatus();
            MissionLifecycle.refreshDeadline(mission, today);
            if (course != null && course.hasEnded(today)) MissionLifecycle.fail(mission);
            if (!previousStatus.equals(mission.getStatus())) changed = true;
        }
        return changed;
    }

    public static boolean complete(Mission mission, CourseCycle course, LocalDate today) {
        if (mission == null) return false;
        MissionLifecycle.refreshDeadline(mission, today);
        if (course == null) return false;
        if (course.hasEnded(today)) {
            MissionLifecycle.fail(mission);
            return false;
        }
        if (!course.isActive(today)) return false;
        return MissionLifecycle.complete(mission, today);
    }
}
