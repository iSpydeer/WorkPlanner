package com.ispydeer.WorkPlanner.controllers;

import com.ispydeer.WorkPlanner.entities.team.dto.TeamDto;
import com.ispydeer.WorkPlanner.entities.user.dto.UserDto;
import com.ispydeer.WorkPlanner.entities.user.dto.UserRegistrationDto;
import com.ispydeer.WorkPlanner.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves all users
     *
     * @return a list of UserDto objects and HTTP 200 OK status.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> retrieveAllUsers() {
        List<UserDto> listOfUsers = userService.retrieveAllUsers();
        return new ResponseEntity<>(listOfUsers, HttpStatus.OK);
    }

    /**
     * Retrieves a specific user by its ID.
     *
     * @param userId the ID of the user to retrieve.
     * @return the UserDto object corresponding to the given ID and HTTP 200 OK status.
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> retrieveUser(@PathVariable Integer userId) {
        UserDto userDto = userService.retrieveUserById(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * Creates a new user with the provided registration details.
     *
     * @param userRegistrationDto the registration details of the new user
     * @return HTTP 201 CREATED status.
     */
    @PostMapping("/users")
    public ResponseEntity<UserRegistrationDto> createUser(
            @Valid @RequestBody UserRegistrationDto userRegistrationDto
    ) {
        userService.createUser(userRegistrationDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Deletes a user by its ID. Only users with 'ADMIN' scope can access this endpoint.
     *
     * @param userId the ID of the user to delete
     * @return HTTP 204 NO CONTENT status
     */
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Retrieves all teams that a specific user belongs to.
     *
     * @param userId the ID of the user
     * @return a list of TeamDto and HTTP 200 OK status.
     */
    @GetMapping("/users/{userId}/teams")
    public ResponseEntity<List<TeamDto>> retrieveUserTeams(@PathVariable int userId) {
        List<TeamDto> teams = userService.retrieveUserTeams(userId);
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    /**
     * Adds a user to a specific team.
     *
     * @param userId the ID of the user to add
     * @param teamId the ID of the team to add the user to
     * @return HTTP 200 OK status.
     */
    @PutMapping("/users/{userId}/teams/{teamId}")
    public ResponseEntity<UserDto> addUserTeam(
            @PathVariable int userId,
            @PathVariable int teamId
    ) {
        userService.addUserTeam(userId, teamId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Removes a user from a specific team.
     *
     * @param userId the ID of the user
     * @param teamId the ID of the team to remove the user from
     * @return HTTP 204 NO CONTENT status
     */
    @DeleteMapping("/users/{userId}/teams/{teamId}")
    public ResponseEntity<UserDto> deleteUserTeam(
            @PathVariable int userId,
            @PathVariable int teamId
    ) {
        userService.deleteUserTeam(userId, teamId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
