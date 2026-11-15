package com.brewcoders.ascend.backend.service;

import java.time.LocalDate;
import java.io.IOException;
import com.brewcoders.ascend.backend.storage.AccountStore;
import com.brewcoders.ascend.model.Mission;
import java.util.List;
import java.util.ArrayList;
import com.brewcoders.ascend.backend.model.Account;
import com.brewcoders.ascend.backend.model.CourseCycle;
import com.brewcoders.ascend.backend.model.User;

/*	Handles registration, login, sessions, course setup, and account saving.
 */
public class AuthService {
    private static final AuthService INSTANCE = new AuthService();
    private final ArrayList<Account> accounts = new ArrayList<>();
    private User currentUser;
    private final AccountStore store = new AccountStore();
    private boolean storageReady = true;
    private boolean pendingSave;
    private String storageMessage = "";

    private AuthService() {
        // Purpose: Load all accounts without restoring a signed-in session.
        try {
            accounts.addAll(store.load());
            boolean changed = false;
            for (int i = 0; i < accounts.size(); i++) {
                User user = accounts.get(i).getUser();
                if (MissionRules.refresh(user.getMissions(), user.getCurrentCourse(), LocalDate.now())) changed = true;
            }
            if (changed) saveData();
        } catch (IOException e) {
            storageReady = false;
            storageMessage = "Cannot load saved accounts. Original file was not overwritten: " + e.getMessage();
            System.err.println(storageMessage);
        }
    }

    public static AuthService getInstance() {
        return INSTANCE;
    }

    /* Purpose: Reject duplicate names, build a valid account, then sign it in.
     */
    public User register(String username, String password) {
        requireStorageReady();
        if (findAccount(username) != null) {
            throw new IllegalArgumentException("That username is already registered.");
        }
        Account account = new Account(username, password);
        accounts.add(account);
        if (!saveData()) {
            String error = storageMessage;
            accounts.remove(account);
            saveData();
            throw new IllegalStateException(error);
        }
        currentUser = account.getUser();
        return currentUser;
    }

    /* Purpose: Verify both credentials before changing the signed-in user.
     */
    public User login(String username, String password) {
        requireStorageReady();
        Account account = findAccount(username);
        if (account == null || !account.matchesPassword(password)) {
            throw new IllegalArgumentException("Incorrect username or password.");
        }
        currentUser = account.getUser();
        if (MissionRules.refresh(currentUser.getMissions(), currentUser.getCurrentCourse(), LocalDate.now())) saveData();
        retryPendingSave();
        return currentUser;
    }

    /* Purpose: Find names case-insensitively with a simple loop; null is not an account.
     */
    private Account findAccount(String username) {
        if (username == null) {
            return null;
        }
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            if (account.getUser().getUsername().equalsIgnoreCase(username)) {
                return account;
            }
        }
        return null;
    }

    /* Purpose: Read the signed-in profile, or null when nobody is signed in.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /* Purpose: Require a session for operations that change personal course data.
     */
    public User requireCurrentUser() {
        if (currentUser == null) {
            throw new IllegalStateException("Please log in first.");
        }
        return currentUser;
    }

    /* Validate the exact course data received from the DatePickers and save it.
     */
    public CourseCycle createCourse(String courseName, LocalDate startDate, LocalDate endDate) {
        User user = requireCurrentUser();
        CourseCycle course = new CourseCycle(courseName, startDate, endDate);
        user.createCourse(course);
        if (!saveData()) {
            String error = storageMessage;
            user.deleteCurrentCourse();
            saveData();
            throw new IllegalStateException(error);
        }
        return course;
    }

    /* Purpose: Clear this user's current course and its mission progress.
     */
    public void deleteCurrentCourse() {
        User user = requireCurrentUser();
        CourseCycle oldCourse = user.getCurrentCourse();
        List<Mission> oldMissions = user.getMissions();
        user.deleteCurrentCourse();
        if (!saveData()) {
            if (oldCourse != null) user.createCourse(oldCourse);
            user.setMissions(oldMissions);
            saveData();
        }
    }

    /* End the session while retaining saved accounts and retrying pending writes.
     */
    public void logout() {
        retryPendingSave();
        currentUser = null;
    }

    /* Purpose: Prevent invalid loaded data from being replaced by new accounts.
     */
    private void requireStorageReady() {
        if (!storageReady) throw new IllegalStateException(storageMessage);
    }

    /* Purpose: Save all account changes and remember pending progress if writing fails.
     */
    public boolean saveData() {
        if (!storageReady) return false;
        try {
            store.save(accounts);
            pendingSave = false;
            storageMessage = "Saved to " + store.getFilePath();
            return true;
        } catch (IOException e) {
            pendingSave = true;
            String message = "Progress is not saved yet: " + e.getMessage();
            if (!message.equals(storageMessage)) System.err.println(message);
            storageMessage = message;
            return false;
        }
    }

    public void retryPendingSave() {
        if (pendingSave) saveData();
    }

    public String getStorageMessage() { return storageMessage; }

    public boolean hasPendingSave() { return pendingSave; }

    public String getStoragePath() { return store.getFilePath(); }
}
