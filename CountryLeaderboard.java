package com.amigoscode;

public class CountryLeaderboard {
    private String countryName;
    private int totalTournamentScore;

    public CountryLeaderboard(String countryName, int totalTournamentScore) {
        this.countryName = countryName;
        this.totalTournamentScore = totalTournamentScore;
    }

    public String getCountryName() {
        return countryName;
    }

    public int getTotalTournamentScore() {
        return totalTournamentScore;
    }
}