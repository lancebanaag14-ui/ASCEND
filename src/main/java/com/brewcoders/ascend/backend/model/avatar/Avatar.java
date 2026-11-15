package com.brewcoders.ascend.backend.model.avatar;

// Abstraction: every avatar must provide these two details.

public abstract class Avatar {
    public abstract String getTitle();

    public abstract int getRequiredPoints();
    
    public boolean isUnlocked(int points) {
        return points >= getRequiredPoints();
    }
}
