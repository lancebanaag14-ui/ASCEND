package com.brewcoders.ascend.backend.service;

import java.time.LocalDate;
import com.brewcoders.ascend.model.Mission;

/* 	Controls mission completion, failure, and allowed status changes.
 */
public class MissionLifecycle {

    private MissionLifecycle() {}

    public static void validateTransition(Mission mission, String next) {
        if (!"ACTIVE".equals(next) && !"COMPLETED".equals(next) && !"FAILED".equals(next)) {
            throw new IllegalArgumentException("Unknown mission status.");
        }
        if (mission.getStatus().equals(next)) return;
        if (!"ACTIVE".equals(mission.getStatus())) {
            throw new IllegalStateException("This mission cannot change to " + next + ".");
        }
    }

    public static void refreshDeadline(Mission mission, LocalDate today) {
        if (today == null) throw new IllegalArgumentException("A date is required.");
        if ("ACTIVE".equals(mission.getStatus()) && mission.getDueDate().isBefore(today)) {
            mission.setStatus("FAILED");
        }
    }

    /* Purpose: Complete once only if the deadline has not passed.
     */
    public static boolean complete(Mission mission, LocalDate today) {
        refreshDeadline(mission, today);
        if (!"ACTIVE".equals(mission.getStatus())) return false;
        mission.setStatus("COMPLETED");
        return true;
    }

    /* Purpose: Fail an active task without rewriting completed or failed history.
     */
    public static boolean fail(Mission mission) {
        if (!"ACTIVE".equals(mission.getStatus())) return false;
        mission.setStatus("FAILED");
        return true;
    }
}
