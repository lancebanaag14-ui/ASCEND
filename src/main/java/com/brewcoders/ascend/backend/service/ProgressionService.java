package com.brewcoders.ascend.backend.service;

import java.util.ArrayList;
import java.util.List;
import com.brewcoders.ascend.model.Mission;
import com.brewcoders.ascend.backend.model.Achievement;
import com.brewcoders.ascend.backend.model.avatar.*;

/* 	Calculates earned points, the current avatar rank, and unlocked achievements.
 */
public class ProgressionService {

    private ProgressionService() {}

    // Purpose: Calculate the total points earned from completed missions.
    public static int totalPoints(List<Mission> missions) {
        int total = 0;
        for (int i = 0; i < missions.size(); i++) {
            if (missions.get(i).isCompleted()) total += missions.get(i).getPoints();
        }
        return total;
    }

    // Purpose: Determine the current avatar based on total points earned.
    public static Avatar currentAvatar(int points) {
        if (points >= 3000) return new Wildcard();
        if (points >= 2000) return new Grinder();
        if (points >= 1000) return new NightCoder();
        return new Noob();
    }

    // Purpose: Determine the achievements unlocked based on total points earned.
    public static List<Achievement> achievements(int points) {
        Avatar[] avatars = {new Noob(), new NightCoder(), new Grinder(), new Wildcard()};
        List<Achievement> results = new ArrayList<>();
        for (int i = 0; i < avatars.length; i++) results.add(new Achievement(avatars[i], points));
        return results;
    }
}
