package com.brewcoders.ascend.backend.model.avatar;

// Inheritance: Grinder inherits isUnlocked() from Avatar.

public class Grinder extends Avatar {
    
    @Override
    public String getTitle() {
        return "The Grinder";
    }

    @Override
    public int getRequiredPoints() {
        return 2000;
    }
}
