package com.brewcoders.ascend.backend.model.avatar;

// Inheritance: Noob inherits isUnlocked() from Avatar.
public class Noob extends Avatar {

    @Override
    public String getTitle() {
        return "The Noob";
    }

    @Override
    public int getRequiredPoints() {
        return 0;
    }
}
