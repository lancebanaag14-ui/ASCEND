package com.brewcoders.ascend.backend.model.avatar;

// Inheritance: Wildcard inherits isUnlocked() from Avatar.

public class Wildcard extends Avatar {

    @Override
    public String getTitle() {
        return "The Wildcard";
    }

    @Override
    public int getRequiredPoints() {
        return 3000;
    }
}
