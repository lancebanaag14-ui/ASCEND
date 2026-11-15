package com.brewcoders.ascend.backend.model;

import com.brewcoders.ascend.backend.model.avatar.Avatar;

/* Purpose: Describe one rank achievement at the current course's points total.
 */
public class Achievement {
    private final Avatar avatar;
    private final boolean unlocked;

    /* Purpose: Calculate unlock state using the rank's own threshold.
     */
    public Achievement(Avatar avatar, int points) {
        this.avatar = avatar;
        unlocked = avatar.isUnlocked(points);
    }

    public String getTitle() { return avatar.getTitle(); }

    public int getRequiredPoints() { return avatar.getRequiredPoints(); }

    public boolean isUnlocked() { return unlocked; }
}
