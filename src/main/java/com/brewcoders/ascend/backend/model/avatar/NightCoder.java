package com.brewcoders.ascend.backend.model.avatar;

// Inheritance: NightCoder inherits isUnlocked() from Avatar.
public class NightCoder extends Avatar {
    
    @Override
    public String getTitle() {
        return "The Night Coder";
    }

    @Override
    public int getRequiredPoints() {
        return 1000;
    }
}
