package com.ispydeer.WorkPlanner.controllers;

import com.ispydeer.WorkPlanner.entities.user.dto.UserDto;
import com.ispydeer.WorkPlanner.services.TeamService;
import com.ispydeer.WorkPlanner.entities.team.dto.TeamDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * Retrieves all teams
     *
     * @return a list of TeamDto objects and HTTP 200 OK status.
     */
    @GetMapping("/teams")
    public ResponseEntity<List<TeamDto>> retrieveAllTeams() {
        List<TeamDto> teams = teamService.retrieveAllTeams();
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    /**
     * Retrieves a specific team by its ID.
     *
     * @param teamId the ID of the team to retrieve.
     * @return the TeamDto object corresponding to the given ID and HTTP 200 OK status.
     */
    @GetMapping("/teams/{teamId}")
    public ResponseEntity<TeamDto> retrieveTeam(@PathVariable Integer teamId) {
        TeamDto teamDto = teamService.retrieveTeamById(teamId);
        return new ResponseEntity<>(teamDto, HttpStatus.OK);
    }

    /**
     * Creates a new team. Only users with "ADMIN" scope can access this endpoint.
     *
     * @param teamDto the DTO containing team details.
     * @return HTTP 201 CREATED status.
     */
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping("/teams")
    public ResponseEntity<TeamDto> createTeam(
            @Valid @RequestBody TeamDto teamDto
    ) {
        teamService.createTeam(teamDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Deletes a team by its ID. Only users with "ADMIN" scope can access this endpoint.
     *
     * @param teamId the ID of the plan entry to delete
     * @return HTTP 204 NO CONTENT status
     */
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/teams/{teamId}")
    public ResponseEntity<TeamDto> deleteTeam(@PathVariable Integer teamId) {
        teamService.deleteTeam(teamId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Assigns a team leader to a specific team. Only users with "ADMIN" scope can access this endpoint.
     *
     * @param teamId the ID of the team
     * @param userId the ID of the user to be assigned as team leader
     * @return HTTP 200 OK status
     */
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PutMapping("/teams/{teamId}/team-leader/{userId}")
    public ResponseEntity<TeamDto> setTeamLeader(
            @PathVariable Integer teamId,
            @PathVariable Integer userId) {
        teamService.setTeamLeader(teamId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Unassigns a team leader from a specific team. Only users with "ADMIN" scope can access this endpoint.
     *
     * @param teamId the ID of the team
     * @return HTTP 204 NO CONTENT status
     */
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/teams/{teamId}/team-leader")
    public ResponseEntity<TeamDto> resetTeamLeader(@PathVariable Integer teamId) {
        teamService.resetTeamLeader(teamId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Adds multiple users to a team.
     *
     * @param teamId the ID of the team
     * @param listOfUserIds a list of user IDs to be added to the team
     * @return HTTP 200 OK status
     */
    @PutMapping("/teams/{teamId}/users")
    public ResponseEntity<TeamDto> addTeamUsers(
            @PathVariable Integer teamId,
            @RequestBody List<Integer> listOfUserIds
    ) {
        teamService.addTeamUsers(teamId, listOfUserIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Retrieves users associated with a specific team.
     *
     * @param teamId the ID of the team
     * @return a list of UserDto and HTTP 200 OK status.
     */
    @GetMapping("/teams/{teamId}/users")
    public ResponseEntity<List<UserDto>> retrieveTeamUsers(@PathVariable Integer teamId) {
        List<UserDto> teamUsers = teamService.retrieveTeamUsers(teamId);
        return new ResponseEntity<>(teamUsers, HttpStatus.OK);
    }

    /**
     * Removes a user from a team.
     *
     * @param teamId the ID of the team
     * @param userId the ID of the user to be removed
     * @return HTTP 204 NO CONTENT status
     */
    @DeleteMapping("/teams/{teamId}/users/{userId}")
    public ResponseEntity<TeamDto> deleteTeamUsers(
            @PathVariable Integer teamId,
            @PathVariable Integer userId
    ) {
        teamService.deleteTeamUser(teamId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
