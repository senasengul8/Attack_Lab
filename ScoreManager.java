package com.amigoscode;

import java.util.HashMap;
import java.util.Map;

public class ScoreManager {
    private static Map<User, Integer> userScores = new HashMap<>();

    public static int getScore(User user) {
        return userScores.getOrDefault(user, 0); // Return 0 if the user is not found
    }

    public void incrementScore(User user) {
        int currentScore = getScore(user);
        userScores.put(user, currentScore + 1);
    }
}