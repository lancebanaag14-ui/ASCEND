package com.brewcoders.ascend.backend.model;

import java.util.ArrayList;
import java.util.List;
import com.brewcoders.ascend.backend.service.MissionOwnership;
import com.brewcoders.ascend.model.Mission;
import com.brewcoders.ascend.backend.model.avatar.Avatar;
import com.brewcoders.ascend.backend.model.avatar.Noob;

/* Purpose: Keep a user's identity, course, starting rank, and course missions together.
 */
public class User {
    private final String username;
    private CourseCycle currentCourse;
    private final Avatar startingAvatar;
    private final ArrayList<Mission> missions = new ArrayList<>();

    /* Purpose: Create a valid profile with the default Noob avatar.
     */
    public User(String username) {
        if (username == null || !username.matches("[a-zA-Z0-9_]{3,30}")) {
            throw new IllegalArgumentException("Username must be 3-30 letters, numbers, or underscores.");
        }
        this.username = username;
        startingAvatar = new Noob();
    }

    public String getUsername() { return username; }

    public CourseCycle getCurrentCourse() { return currentCourse; }

    public Avatar getStartingAvatar() { return startingAvatar; }

    public boolean hasCourse() { return currentCourse != null; }

    public void createCourse(CourseCycle course) {
        if (course == null) {
            throw new IllegalArgumentException("A course is required.");
        }
        if (hasCourse()) {
            throw new IllegalStateException("This user already has a current course.");
        }
        currentCourse = course;
    }

    public List<Mission> getMissions() { return new ArrayList<>(missions); }

    /* Purpose: Add a task only when the user has a course.
     */
    public void addMission(Mission mission) {
        if (!hasCourse()) {
            throw new IllegalStateException("Create a course before adding missions.");
        }
        if (mission == null) {
            throw new IllegalArgumentException("A mission is required.");
        }
        if (missions.contains(mission)) {
            throw new IllegalArgumentException("This mission is already stored.");
        }
        MissionOwnership.claim(mission, username);
        missions.add(mission);
    }

    /* Purpose: Remove mission progress for this user only.
     */
    public void clearMissions() { missions.clear(); }

    /* Purpose: Replace this user's mission list, validating it before clearing old entries.
     */
    public void setMissions(List<Mission> userMissions) {
        ArrayList<Mission> replacement = new ArrayList<>();
        if (userMissions != null) {
            for (int i = 0; i < userMissions.size(); i++) {
                Mission mission = userMissions.get(i);
                if (mission == null) {
                    throw new IllegalArgumentException("A mission is required.");
                }
                if (replacement.contains(mission)) {
                    throw new IllegalArgumentException("The mission list contains a duplicate object.");
                }
                if (currentCourse != null && (mission.getDueDate().isBefore(currentCourse.getStartDate())
                        || mission.getDueDate().isAfter(currentCourse.getEndDate()))) {
                    throw new IllegalArgumentException("A loaded mission is outside the course dates.");
                }
                if (!MissionOwnership.canBelongTo(mission, username)) {
                    throw new IllegalArgumentException("A loaded mission belongs to another account.");
                }
                replacement.add(mission);
            }
        }
        if (!replacement.isEmpty() && !hasCourse()) {
            throw new IllegalStateException("Create a course before loading missions.");
        }
        for (int i = 0; i < replacement.size(); i++) MissionOwnership.claim(replacement.get(i), username);
        missions.clear();
        missions.addAll(replacement);
    }

    /* Purpose: Delete only this user's course and progress, retaining the account.
     */
    public void deleteCurrentCourse() {
        missions.clear();
        currentCourse = null;
    }
}
