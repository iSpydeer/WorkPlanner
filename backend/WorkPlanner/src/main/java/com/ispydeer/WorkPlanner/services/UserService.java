package com.ispydeer.WorkPlanner.services;

import com.ispydeer.WorkPlanner.controllers.exceptions.UserNotFoundException;
import com.ispydeer.WorkPlanner.controllers.exceptions.UsernameAlreadyUsedException;
import com.ispydeer.WorkPlanner.entities.team.Team;
import com.ispydeer.WorkPlanner.entities.team.dto.TeamDto;
import com.ispydeer.WorkPlanner.entities.user.User;
import com.ispydeer.WorkPlanner.entities.user.dto.UserDto;
import com.ispydeer.WorkPlanner.entities.user.dto.UserRegistrationDto;
import com.ispydeer.WorkPlanner.entities.user.role.Role;
import com.ispydeer.WorkPlanner.repositiories.TeamRepository;
import com.ispydeer.WorkPlanner.repositiories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * Service class for managing user-related operations.
 */
@Service
public class UserService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructs a UserService with the specified dependencies.
     *
     * @param userRepository      the repository for user operations
     * @param teamRepository      the repository for team operations
     * @param modelMapper         the model mapper for DTO conversions
     * @param passwordEncoder     the password encoder for hashing passwords
     */
    public UserService(
            UserRepository userRepository,
            TeamRepository teamRepository,
            ModelMapper modelMapper,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.teamRepository = teamRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieves all users, sorted by their first name.
     *
     * @return a list of UserDto representing all users
     */
    public List<UserDto> retrieveAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .sorted(Comparator.comparing(UserDto::getFirstName))
                .toList();
    }

    /**
     * Retrieves a user by ID.
     *
     * @param userId the ID of the user
     * @return a UserDto representing the found user
     * @throws UserNotFoundException if the user with the given ID does not exist
     */
    public UserDto retrieveUserById(int userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return modelMapper.map(user, UserDto.class);
    }

    /**
     * Retrieves a user by username.
     *
     * @param username the username of the user
     * @return a UserDto representing the found user
     * @throws UserNotFoundException if the user with the given username does not exist
     */
    public UserDto retrieveUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return modelMapper.map(user, UserDto.class);
    }

    /**
     * Creates a new user based on the provided registration data.
     *
     * @param userRegistrationDto the registration data for the new user
     * @throws UsernameAlreadyUsedException if the username is already in use
     */
    public void createUser(UserRegistrationDto userRegistrationDto) {
        if (userRepository.existsByUsername(userRegistrationDto.getUsername())) {
            throw new UsernameAlreadyUsedException();
        }
        User user = modelMapper.map(userRegistrationDto, User.class);
        user.setAccountCreationDate(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    /**
     * Deletes a user by ID and removes from any associated teams.
     *
     * @param userId the ID of the user to be deleted
     * @throws UserNotFoundException if the user with the given ID does not exist
     */
    public void deleteUser(int userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        for (Team team : user.getSetOfTeams()) {
            if (team.getTeamLeader() != null && team.getTeamLeader().equals(user)) {
                team.setTeamLeader(null);
            }
            team.getSetOfUsers().remove(user);
            teamRepository.save(team);
        }
        userRepository.deleteById(userId);
    }

    /**
     * Retrieves all teams associated with a specific user.
     *
     * @param userId the ID of the user
     * @return a list of TeamDto representing the user's teams
     * @throws UserNotFoundException if the user with the given ID does not exist
     */
    public List<TeamDto> retrieveUserTeams(int userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return user.getSetOfTeams().stream()
                .map(team -> modelMapper.map(team, TeamDto.class))
                .sorted(Comparator.comparing(TeamDto::getName))
                .toList();
    }

    /**
     * Adds a user to a specified team.
     *
     * @param userId the ID of the user to add
     * @param teamId the ID of the team to which the user will be added
     * @throws UserNotFoundException if the user or team does not exist
     */
    public void addUserTeam(int userId, int teamId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Team team = teamRepository.findById(teamId).orElseThrow(UserNotFoundException::new);
        user.getSetOfTeams().add(team);
        userRepository.save(user);
    }

    /**
     * Removes a user from a specified team.
     *
     * @param userId the ID of the user to remove
     * @param teamId the ID of the team from which the user will be removed
     * @throws UserNotFoundException if the user or team does not exist
     */
    public void deleteUserTeam(int userId, int teamId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Team team = teamRepository.findById(teamId).orElseThrow(UserNotFoundException::new);
        user.getSetOfTeams().remove(team);
        userRepository.save(user);
    }
}
