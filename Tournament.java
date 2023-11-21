package com.amigoscode;

import java.time.LocalTime;
import java.util.*;

public class Tournament {
    private static List<User> allUsers;
    private LocalTime startTime; // 00:00 UTC
    private LocalTime endTime;   // 20:00 UTC
    private static int minLevelRequired;
    private static int entryFee;
    private static List<TournamentGroup> groups; // List of tournament groups
    private boolean active;

    public Tournament() {
        this.startTime = LocalTime.of(0, 0); // 00:00 UTC
        this.endTime = LocalTime.of(20, 0);   // 20:00 UTC
        this.minLevelRequired = 20;
        this.entryFee = 1000; // 1,000 coins
        this.groups = new ArrayList<>();
        this.active = false;
    }

    public void addTournamentGroup(TournamentGroup group) {
        groups.add(group);
    }

    public static TournamentGroup findUserGroup(int userId) {
        for (TournamentGroup group : groups) {
            for (User user : group.getTusers()) {
                if (user.getUserID() == userId) {
                    return group;
                }
            }
        }
        return null; // User not found in any group
    }

    public static List<CountryLeaderboard> calculateCountryLeaderboard() {
        Map<String, Integer> countryScores = new HashMap<>();

        // Iterate through the list of all users
        for (User user : allUsers) {
            String country = user.getCountry();
            int userScore = ScoreManager.getScore(user);

            // Update the total tournament score for the country
            countryScores.put(country, countryScores.getOrDefault(country, 0) + userScore);
        }

        // Create a list to store the leaderboard data
        List<CountryLeaderboard> leaderboard = new ArrayList<>();

        // Iterate through the map and create CountryLeaderboard objects
        for (Map.Entry<String, Integer> entry : countryScores.entrySet()) {
            leaderboard.add(new CountryLeaderboard(entry.getKey(), entry.getValue()));
        }

        // Sort the leaderboard based on total scores in descending order
        leaderboard.sort(Comparator.comparingInt(CountryLeaderboard::getTotalTournamentScore).reversed());

        return leaderboard;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getMinLevelRequired() {
        return minLevelRequired;
    }

    public int getEntryFee() {
        return entryFee;
    }


    public boolean isActive() {
        LocalTime currentTime = LocalTime.now();
        return !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
    }


    public List<TournamentGroup> getGroups() {
        return groups;
    }

    public static boolean isUserEligibleToJoin(User user) {
        // Check if the user meets eligibility criteria (level and entry fee)
        return user.getLevel() >= minLevelRequired && user.getCoins() >= entryFee;
    }

    public boolean joinTournament(User user) {
        if (!isUserEligibleToJoin(user)) {
            return false;
        }

        // Assign a random group to the user
        assignRandomGroup(user);

        return true;
    }

    private void assignRandomGroup(User user) {
        // Implement logic to assign users to groups (one from each country)
        // For simplicity, you can create and manage groups separately.
    }
}

