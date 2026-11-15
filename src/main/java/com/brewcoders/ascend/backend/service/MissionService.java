package com.brewcoders.ascend.backend.service;

import com.brewcoders.ascend.model.Mission;
import com.brewcoders.ascend.backend.model.User;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.brewcoders.ascend.backend.model.CourseCycle;
import java.util.ArrayList;
import java.util.List;
import com.brewcoders.ascend.backend.model.avatar.Avatar;
import com.brewcoders.ascend.backend.model.Achievement;

// Coordinates mission operations, history, points, progression, and saving behind the manager.

public class MissionService {
    private static MissionService instance;
    private String lastActionMessage = "";
    private MissionService() {}
    public static synchronized MissionService getInstance() {
        if (instance == null) {
            instance = new MissionService();
        }
        return instance;
    }

    public String getCurrentUsername() {
        User user = AuthService.getInstance().getCurrentUser();
        if (user == null) {
            return "Guest User";
        }
        return user.getUsername();
    }

    public void setCurrentUsername(String username) {
        if ("Guest User".equals(username)) {
            logout();
        } else if (username == null || !getCurrentUsername().equalsIgnoreCase(username.trim())) {
            throw new IllegalStateException("Use login or registration to select an account.");
        }
    }

    public void clearMissions() {
        AuthService.getInstance().requireCurrentUser().clearMissions();
        AuthService.getInstance().saveData();
    }

    public void logout() {
        lastActionMessage = "";
        AuthService.getInstance().logout();
    }

    public void deleteCurrentCourseAndLogout() {
        AuthService.getInstance().deleteCurrentCourse();
        logout();
    }

    public void setMissions(List<Mission> userMissions) {
        AuthService.getInstance().requireCurrentUser().setMissions(userMissions);
        AuthService.getInstance().saveData();
    }

    public void addMission(Mission mission) {
        User user = AuthService.getInstance().requireCurrentUser();
        if (MissionRules.refresh(user.getMissions(), user.getCurrentCourse(), LocalDate.now())) {
            AuthService.getInstance().saveData();
        }
        MissionRules.validateNewMission(mission, user.getCurrentCourse(), LocalDate.now());
        user.addMission(mission);
        AuthService.getInstance().saveData();
    }

    public List<Mission> getAllMissions() {
        User user = AuthService.getInstance().getCurrentUser();
        if (user == null) {
            return new ArrayList<>();
        }
        List<Mission> missions = user.getMissions();
        if (MissionRules.refresh(missions, user.getCurrentCourse(), LocalDate.now())) {
            AuthService.getInstance().saveData();
        }
        AuthService.getInstance().retryPendingSave();
        return missions;
    }

    public List<Mission> getActiveMissions() {
        List<Mission> missions = getAllMissions();
        List<Mission> active = new ArrayList<>();
        for (int i = 0; i < missions.size(); i++) {
            Mission mission = missions.get(i);
            if ("ACTIVE".equalsIgnoreCase(mission.getStatus())) {
                active.add(mission);
            }
        }
    
        return MissionSorter.byDueDate(active);
    }

    
    public List<Mission> getHistoryMissions() {
        List<Mission> history = new ArrayList<>();
        List<Mission> missions = getAllMissions();
        for (int i = 0; i < missions.size(); i++) {
            Mission mission = missions.get(i);
            if (mission.isCompleted() || mission.isFailed()) {
                history.add(mission);
            }
        }
        return history;
    }


    public int getTotalPoints() {
        return ProgressionService.totalPoints(getAllMissions());
    }

    
    public void completeMission(Mission mission) {
        tryCompleteMission(mission);
    }

    public boolean tryCompleteMission(Mission mission) {
        User user = AuthService.getInstance().getCurrentUser();
        if (user == null || mission == null || !user.getMissions().contains(mission)) {
            lastActionMessage = "Log in as this mission's owner before completing it.";
            return false;
        }
        if (MissionRules.refresh(user.getMissions(), user.getCurrentCourse(), LocalDate.now())) {
            AuthService.getInstance().saveData();
        }
        if (!MissionRules.complete(mission, user.getCurrentCourse(), LocalDate.now())) {
            lastActionMessage = "Mission not completed: it is finished, overdue, or outside the course dates.";
            return false;
        }
        boolean saved = AuthService.getInstance().saveData();
        lastActionMessage = "Mission completed. Earned " + mission.getPoints() + " points.";
        if (!saved) lastActionMessage += " Disk save is pending.";
        return true;
    }

    public String getLastActionMessage() { return lastActionMessage; }

    public void failMission(Mission mission) {
        requireOwnedMission(mission);
        if (MissionLifecycle.fail(mission)) AuthService.getInstance().saveData();
    }

    private void requireOwnedMission(Mission mission) {
        User user = AuthService.getInstance().requireCurrentUser();
        if (mission == null || !user.getMissions().contains(mission)) {
            throw new IllegalArgumentException("This mission does not belong to the current user.");
        }
    }

    public Avatar getCurrentAvatar() { return ProgressionService.currentAvatar(getTotalPoints()); }

    public List<Achievement> getAchievements() { return ProgressionService.achievements(getTotalPoints()); }

    public void authenticate(String username, String password, boolean loginMode) {
        if (loginMode) {
            AuthService.getInstance().login(username, password);
        } else {
            AuthService.getInstance().register(username, password);
        }
    }

    public boolean hasCurrentCourse() {
        User user = AuthService.getInstance().getCurrentUser();
        return user != null && user.hasCourse();
    }

    public String getCurrentCourseName() {
        if (!hasCurrentCourse()) throw new IllegalStateException("Create a course first.");
        return AuthService.getInstance().requireCurrentUser().getCurrentCourse().getCourseName();
    }

    public String getCurrentCourseDates() {
        if (!hasCurrentCourse()) throw new IllegalStateException("Create a course first.");
        CourseCycle course = AuthService.getInstance().requireCurrentUser().getCurrentCourse();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return course.getStartDate().format(format) + " - " + course.getEndDate().format(format);
    }

    public void createCourse(String courseName, LocalDate start, LocalDate end) {
        AuthService.getInstance().createCourse(courseName, start, end);
    }
}
