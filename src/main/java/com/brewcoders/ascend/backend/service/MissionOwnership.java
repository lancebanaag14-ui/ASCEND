package com.brewcoders.ascend.backend.service;

import java.util.Map;
import java.util.WeakHashMap;
import com.brewcoders.ascend.model.Mission;

/* Prevents the same mission object from belonging to two different accounts.
 */
public class MissionOwnership {

    private static final Map<Mission, String> owners = new WeakHashMap<>();

    private MissionOwnership() {}

    // Purpose: check if the mission belongs to a respective user, return false if it is null.
    public static boolean canBelongTo(Mission mission, String username) {
        String owner = owners.get(mission);
        return mission != null && username != null && (owner == null || owner.equalsIgnoreCase(username));
    }

    // Purpose: Assigfn the mission to its respecive userr
    public static void claim(Mission mission, String username) {
        if (username == null || !username.matches("[a-zA-Z0-9_]{3,30}")) {
            throw new IllegalArgumentException("A valid mission owner is required.");
        }
        if (!canBelongTo(mission, username)) {
            throw new IllegalArgumentException("This mission already belongs to another account.");
        }
        owners.put(mission, username);
    }
}
