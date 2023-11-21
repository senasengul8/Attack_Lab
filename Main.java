package com.amigoscode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
@RequestMapping("api/v1/users")
public class Main {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public Main(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

    }

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();

    }

    record NewUserRequest() {
    }

    @PostMapping
    public ResponseEntity<User> createUser() {
        User user = new User();
        user.setLevel(1);
        user.setCoins(5000);
        user.setCountry(user.getCountry());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);

    }

    record UpCustomerRequest(Integer level, Integer coins) {
    }

    @PutMapping("{userId}")
    public ResponseEntity<User> updateLevelRequest(@PathVariable("userId") Integer userId,
                                                   @RequestBody UpCustomerRequest request) {

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build(); // Return a 404 response if the user is not found
        }

        User upUser = optionalUser.get();
        upUser.setLevel(request.level); // Set the new level
        upUser.setCoins(request.coins); // Set the new coins

        // Save the updated user to the database
        userRepository.save(upUser);

        // Return the updated user's information
        return ResponseEntity.ok(upUser);


    }

    @GetMapping("/get-group-leaderboard/{groupId}")
    public ResponseEntity<TournamentGroup> getGroupLeaderboard(@PathVariable Integer groupId) {
        // Assuming you have access to the tournament group specified by the groupId.
        Optional<TournamentGroup> optionalGroup = groupRepository.findById(groupId);
        if (optionalGroup.isPresent()) {
            TournamentGroup tournamentGroup = optionalGroup.get();

            // 2. Calculate the group leaderboard for the tournament group.
            List<User> leaderboard = tournamentGroup.getGroupLeaderboard1(); // Implement this method in your TournamentGroup class

            // You can return a GroupLeaderboard object or a list of users as needed
            TournamentGroup groupLeaderboard = new TournamentGroup(leaderboard); // Assuming you have a GroupLeaderboard class

            return ResponseEntity.ok(groupLeaderboard);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    record ClaimRewardRequest(Integer userID, Integer coins) {
    }

    @PostMapping("/claim-reward{userId}")
    public ResponseEntity<User> claimReward(@RequestBody ClaimRewardRequest request) {
        Optional<User> userOptional = userRepository.findById(request.userID);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build(); // User not found
        }

        User user = userOptional.get();

        // Implement logic to check if the user is eligible to claim rewards

        // Find the user's group and award rewards
        TournamentGroup group = Tournament.findUserGroup(user.getUserID());
        assert group != null;
        group.awardRewards();

        // Save the updated user information to the database
        userRepository.save(user);

        // Return the updated user information
        return ResponseEntity.ok(user);


    }
    @GetMapping("/get-group-rank/{userId}")
    public ResponseEntity<Integer> getGroupRank(@PathVariable Integer userId) {
        // Implement logic to retrieve the player's rank for the tournament.
        // This logic will depend on how you determine a player's rank in the tournament.
// Assuming you have a constructor for the Tournament class
        Tournament tournament = new Tournament();

        int userRank = calculateUserRank(userId, tournament);

        return ResponseEntity.ok(userRank);
    }


    private int calculateUserRank(Integer userId, Tournament tournament) {
        List<TournamentGroup> groups = tournament.getGroups();

        for (TournamentGroup group : groups) {
            List<User> groupUsers = group.getTusers();
            for (int i = 0; i < groupUsers.size(); i++) {
                if (groupUsers.get(i).getUserID().equals(userId)) {
                    return i + 1; // Add 1 to convert to 1-based ranking
                }
            }
        }

        return -1; // User not found or not ranked
    }

    @GetMapping("/get-group-leaderboard/{groupId}")
    public ResponseEntity<List<User>> getGroupLeaderboard(@PathVariable int groupId) {
        // Fetch the group by groupId using your repository or service
        Optional<TournamentGroup> optionalGroup = groupRepository.findById(groupId);

        if (optionalGroup.isPresent()) {
            TournamentGroup tournamentGroup = optionalGroup.get();

            // Calculate the group leaderboard for the tournament group
            List<User> leaderboard = tournamentGroup.getGroupLeaderboard1();
            return ResponseEntity.ok(leaderboard);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/get-country-leaderboard")
    public ResponseEntity<List<CountryLeaderboard>> getCountryLeaderboard() {
        // Implement the logic to retrieve the leaderboard data for countries.
        List<CountryLeaderboard> countryLeaderboard = Tournament.calculateCountryLeaderboard();

        if (countryLeaderboard != null) {
            return ResponseEntity.ok(countryLeaderboard);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}










/*
    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Integer id) {
        userRepository.deleteById(id);
    }

    record UpCustomerRequest(String name, String email, Integer age) {
    }
*/


