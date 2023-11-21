package com.amigoscode;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TournamentGroup {
    @Id
    @SequenceGenerator(name = "group_id_sequence",sequenceName = "group_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "group_id_sequence")
    private Integer groupID;
    private List<User> tusers;
    private boolean isCompetitionStarted;
    private int groupScore;
    private ScoreManager scoreManager;

    public TournamentGroup(List<User> users) {

        this.tusers = users;
        this.isCompetitionStarted = false;
        this.groupScore = 0;
        this.scoreManager = new ScoreManager();
        for (User user : users) {
            addUserToGroup(user);
        }

    }


    public void userPassedLevel(User user) {
         // Get the user's current score
         scoreManager.incrementScore(user);// Update the user's score
    }

    public Integer getGroupID() {
        return groupID;
    }

    public List<User> getTusers() {
        return tusers;
    }

    public boolean isCompetitionStarted() {
        return isCompetitionStarted;
    }

    public int getGroupScore() {
        return groupScore;
    }

    public void addUserToGroup(User user) {

        // Check if the group already has 5 users, one from each country
        if (tusers.size() < 5 && !hasUserFromSameCountry(user)) {
            tusers.add(user);
        }
    }

    private boolean hasUserFromSameCountry(User user) {
        String userCountry = user.getCountry();
        for (User existingUser : tusers) {
            if (userCountry.equals(existingUser.getCountry())) {
                return true; // Group already has a user from the same country
            }
        }
        return false; // No user from the same country in the group
    }

    public void updateUserScores(int levelPassed) {
        groupScore += levelPassed;
    }



    public void awardRewards() {
        // Sort the list of users by their scores in descending order (highest to lowest).
        tusers.sort(Comparator.comparingInt(user -> -scoreManager.getScore(user)));

        for (int i = 0; i < tusers.size(); i++) {
            User user = tusers.get(i);

            // Apply rewards for the top two users (customize as needed).
            if (i == 0) {
                user.addCoins(10000); // First-place reward
            } else if (i == 1) {
                user.addCoins(5000); // Second-place reward
            }
        }
    }



    public List<User> getGroupLeaderboard1() {
        // Sort the list of users by their scores in descending order (highest to lowest).
        tusers.sort(Comparator.comparingInt(user -> -scoreManager.getScore(user)));

        // Create a list to store the leaderboard data.
        List<User> leaderboard = new ArrayList<>();
        for (int i = 0; i < tusers.size(); i++) {
            User user = tusers.get(i);
            int score = scoreManager.getScore(user);
            leaderboard.add(user);

            // Apply rewards for the top two users (customize as needed).
            if (i == 0) {
                user.addCoins(10000); // First-place reward
            } else if (i == 1) {
                user.addCoins(5000); // Second-place reward
            }
        }

        return leaderboard;
    }


}
