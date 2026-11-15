package com.brewcoders.ascend.controller;

import com.brewcoders.ascend.model.Mission;
import java.time.LocalDate;
import java.util.List;
// NEW CHANGED
// Purpose: Delegate extra account, mission, storage and progression logic to the backend.
import com.brewcoders.ascend.backend.service.MissionService;
import com.brewcoders.ascend.backend.model.avatar.Avatar;
import com.brewcoders.ascend.backend.model.Achievement;

public class MissionManager {
    private static MissionManager instance;
    // NEW CHANGED
    // Purpose: Share the backend state instead of maintaining a second mission list and username.
    private final MissionService backend = MissionService.getInstance();

    private MissionManager() {}

    public static synchronized MissionManager getInstance() {
        if (instance == null) {
            instance = new MissionManager();
        }
        return instance;
    }

    // USERNAME CONTROLS 

    public String getCurrentUsername() {
        // NEW CHANGED
        // Purpose: Read the authenticated username from the backend.
        return backend.getCurrentUsername();
    }

    public void setCurrentUsername(String username) {
        // NEW CHANGED
        // Purpose: Keep the original username-sync API with backend authentication checks.
        backend.setCurrentUsername(username);
    }

    // FRONTEND SESSION & USER CONTROLS 

    /**
     * Clears all in-memory missions for a course cycle while preserving the active username.
     * Call this when starting or resetting a course.
     */
    public void clearMissions() {
        // NEW CHANGED
        // Purpose: Clear and save only the current account's course missions.
        backend.clearMissions();
    }

    /**
     * Full logout reset - clears missions AND resets user session identity to default fallback.
     * Call this when logging out or deleting the account.
     */
    public void logout() {
        // NEW CHANGED
        // Purpose: Clear the displayed session while preserving the account's saved progress.
        backend.logout();
    }

    /**
     * Replaces current in-memory missions.
     * Ready for backend integration later when loading saved user history on login.
     */
    public void setMissions(List<Mission> userMissions) {
        // NEW CHANGED
        // Purpose: Validate and save the replacement through the backend.
        backend.setMissions(userMissions);
    }

    // MISSION OPERATIONS 

    public void addMission(Mission mission) {
        // NEW CHANGED
        // Purpose: Let the backend validate ownership, dates and saving.
        backend.addMission(mission);
    }

    public List<Mission> getAllMissions() {
        // NEW CHANGED
        // Purpose: Read the current account's refreshed mission list.
        return backend.getAllMissions();
    }

    public List<Mission> getActiveMissions() {
        // Automatically mark active missions whose due dates have passed as FAILED
        // NEW CHANGED
        // Purpose: Keep filtering, deadline checks and sorting in the backend.
        return backend.getActiveMissions();
    }

    public List<Mission> getHistoryMissions() {
        // Returns all COMPLETED or FAILED missions for the History tab
        // NEW CHANGED
        // Purpose: Read refreshed completed/failed history from the backend.
        return backend.getHistoryMissions();
    }

    /**
     * Sums up points earned from all completed missions.
     */
    public int getTotalPoints() {
        // NEW CHANGED
        // Purpose: Use the backend's single completed-mission point calculation.
        return backend.getTotalPoints();
    }

    public void completeMission(Mission mission) {
        // NEW CHANGED
        // Purpose: Apply guarded completion and saving without changing the existing button call.
        backend.completeMission(mission);
    }

    public void failMission(Mission mission) {
        // NEW CHANGED
        // Purpose: Apply failure rules and saving in the backend.
        backend.failMission(mission);
    }

    // NEW CHANGED
    // Purpose: Pass the existing form credentials to the backend.
    public void authenticate(String username, String password, boolean loginMode) { backend.authenticate(username, password, loginMode); }

    // NEW CHANGED
    // Purpose: Let the existing login handler choose setup or dashboard.
    public boolean hasCurrentCourse() { return backend.hasCurrentCourse(); }

    // NEW CHANGED
    // Purpose: Read the saved course name for the original dashboard constructor.
    public String getCurrentCourseName() { return backend.getCurrentCourseName(); }

    // NEW CHANGED
    // Purpose: Keep saved-date formatting inside the backend.
    public String getCurrentCourseDates() { return backend.getCurrentCourseDates(); }

    // NEW CHANGED
    // Purpose: Pass the original course form values to the backend.
    public void createCourse(String name, LocalDate start, LocalDate end) { backend.createCourse(name, start, end); }

    // NEW CHANGED
    // Purpose: Keep confirmed deletion separate from ordinary logout.
    public void deleteCurrentCourseAndLogout() { backend.deleteCurrentCourseAndLogout(); }

    // NEW CHANGED
    // Purpose: Preserve the boolean completion API for backend callers.
    public boolean tryCompleteMission(Mission mission) { return backend.tryCompleteMission(mission); }

    // NEW CHANGED
    // Purpose: Preserve access to the backend completion outcome.
    public String getLastActionMessage() { return backend.getLastActionMessage(); }

    // NEW CHANGED
    // Purpose: Preserve the earned-avatar API while calculation stays in the backend.
    public Avatar getCurrentAvatar() { return backend.getCurrentAvatar(); }

    // NEW CHANGED
    // Purpose: Preserve achievement access while calculation stays in the backend.
    public List<Achievement> getAchievements() { return backend.getAchievements(); }

}
