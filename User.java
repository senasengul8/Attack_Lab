package com.amigoscode;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Entity
@Table(name="user_Table")
public class User {
    @Id
    @SequenceGenerator(name = "user_id_sequence",sequenceName = "user_id_sequence")
   @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_id_sequence")
    private Integer userID;
    private Integer level;

    private String country;
    private Integer coins;
    private static final List<String> COUNTRIES = Arrays.asList(
            "Turkey", "United States", "United Kingdom", "France", "Germany"
    );


    public User() {
        this.level=1;
        this.coins=5000;
        this.country = getRandomCountry();
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getUserID() {
        return userID;
    }

    public String getCountry() {
        return country;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }
    public void addCoins(int amount) {
        coins += amount;
    }

    private String getRandomCountry() {
        int randomIndex = new Random().nextInt(COUNTRIES.size());
        return COUNTRIES.get(randomIndex);
    }
    public void completeLevel() {
        this.level++;
        this.coins += 25;
    }
}


