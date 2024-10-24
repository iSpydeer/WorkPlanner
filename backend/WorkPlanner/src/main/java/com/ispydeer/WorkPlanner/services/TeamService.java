package com.ispydeer.WorkPlanner.services;

import com.ispydeer.WorkPlanner.controllers.exceptions.TeamNameAlreadyUsedException;
import com.ispydeer.WorkPlanner.controllers.exceptions.TeamNotFoundException;
import com.ispydeer.WorkPlanner.controllers.exceptions.UserNotFoundException;
import com.ispydeer.WorkPlanner.entities.user.dto.UserDto;
import com.ispydeer.WorkPlanner.repositiories.TeamRepository;
import com.ispydeer.WorkPlanner.repositiories.UserRepository;
import com.ispydeer.WorkPlanner.entities.team.Team;
import com.ispydeer.WorkPlanner.entities.team.dto.TeamDto;
import com.ispydeer.WorkPlanner.entities.user.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Service class for managing team-related operations.
 */
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    /**
     * Constructs a TeamService with the specified dependencies.
     *
     * @param teamRepository  the repository for team operations
     * @param userRepository  the repository for user operations
     * @param modelMapper     the model mapper for DTO conversions
     */
    public TeamService(TeamRepository teamRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Retrieves all teams, sorted by their name.
     *
     * @return a list of TeamDto representing all teams
     */
    public List<TeamDto> retrieveAllTeams() {
        List<Team> teams = teamRepository.findAll();
        return teams.stream()
                .map(team -> modelMapper.map(team, TeamDto.class))
                .sorted(Comparator.comparing(TeamDto::getName))
                .toList();
    }

    /**
     * Retrieves a team by ID.
     *
     * @param teamId the ID of the team
     * @return a TeamDto representing the found team
     * @throws TeamNotFoundException if the team with the given ID does not exist
     */
    public TeamDto retrieveTeamById(int teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new);
        return modelMapper.map(team, TeamDto.class);
    }

    /**
     * Retrieves a team by name.
     *
     * @param name the name of the team
     * @return a TeamDto representing the found team
     * @throws TeamNotFoundException if the team with the given name does not exist
     */
    public TeamDto retrieveTeamByName(String name) {
        Team team = teamRepository.findByName(name).orElseThrow(TeamNotFoundException::new);
        return modelMapper.map(team, TeamDto.class);
    }

    /**
     * Creates a new team based on the provided team data.
     *
     * @param teamDto the creation data for the new user
     * @throws TeamNameAlreadyUsedException if the team name is already in use
     */
    public void createTeam(TeamDto teamDto) {
        if (teamRepository.existsByName(teamDto.getName())) {
            throw new TeamNameAlreadyUsedException();
        }
        Team team = modelMapper.map(teamDto, Team.class);
        team.setTeamCreationDate(LocalDateTime.now());
        teamRepository.save(team);
    }

    /**
     * Deletes a team by its ID.
     *
     * @param teamId the ID of the team to be deleted
     */
    public void deleteTeam(int teamId) {
        teamRepository.deleteById(teamId);
    }

    /**
     * Sets the leader of a specified team.
     *
     * @param teamId the ID of the team
     * @param userId the ID of the user to be set as the team leader
     * @throws TeamNotFoundException if the team with the given ID does not exist
     * @throws UserNotFoundException if the user with the given ID does not exist
     */
    public void setTeamLeader(Integer teamId, Integer userId) {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Set<User> setOfUsers = team.getSetOfUsers();
        if (!setOfUsers.contains(user)) {
            setOfUsers.add(user);
            team.setSetOfUsers(setOfUsers);
        }

        team.setTeamLeader(user);
        teamRepository.save(team);
    }

    /**
     * Resets the leader of a specified team to null.
     *
     * @param teamId the ID of the team
     * @throws TeamNotFoundException if the team with the given ID does not exist
     */
    public void resetTeamLeader(int teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new);
        team.setTeamLeader(null);
        teamRepository.save(team);
    }

    /**
     * Retrieves all users associated with a specified team.
     *
     * @param teamId the ID of the team
     * @return a list of UserDto representing the users in the team
     * @throws TeamNotFoundException if the team with the given ID does not exist
     */
    public List<UserDto> retrieveTeamUsers(int teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new);
        Set<User> setOfUsers = team.getSetOfUsers();
        return setOfUsers.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .sorted(Comparator.comparing(UserDto::getFirstName))
                .toList();
    }

    /**
     * Adds multiple users to a specified team.
     *
     * @param teamId        the ID of the team to which users will be added
     * @param listOfUserIds a list of user IDs to be added to the team
     * @throws TeamNotFoundException if the team with the given ID does not exist
     */
    public void addTeamUsers(int teamId, List<Integer> listOfUserIds) {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new);
        List<User> listOfNewUsers = userRepository.findAllById(listOfUserIds);
        Set<User> setOfUsers = team.getSetOfUsers();
        setOfUsers.addAll(listOfNewUsers);
        team.setSetOfUsers(setOfUsers);
        teamRepository.save(team);
    }

    /**
     * Removes a user from a specified team.
     *
     * @param teamId the ID of the team
     * @param userId the ID of the user to be removed
     * @throws TeamNotFoundException if the team with the given ID does not exist
     * @throws UserNotFoundException if the user with the given ID does not exist
     */
    public void deleteTeamUser(int teamId, int userId) {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (team.getTeamLeader() != null && team.getTeamLeader().equals(user)) {
            team.setTeamLeader(null);
        }

        team.getSetOfUsers().remove(user);
        teamRepository.save(team);
    }
}
