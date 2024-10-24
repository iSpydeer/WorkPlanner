package com.ispydeer.WorkPlanner.services;

import com.ispydeer.WorkPlanner.controllers.exceptions.PlanEntryNotFoundException;
import com.ispydeer.WorkPlanner.controllers.exceptions.TeamNotFoundException;
import com.ispydeer.WorkPlanner.controllers.exceptions.UserNotFoundException;
import com.ispydeer.WorkPlanner.entities.planEntry.PlanEntry;
import com.ispydeer.WorkPlanner.entities.planEntry.dto.PlanEntryDto;
import com.ispydeer.WorkPlanner.entities.team.Team;
import com.ispydeer.WorkPlanner.entities.user.User;
import com.ispydeer.WorkPlanner.repositiories.PlanEntryRepository;
import com.ispydeer.WorkPlanner.repositiories.TeamRepository;
import com.ispydeer.WorkPlanner.repositiories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Service class for managing plan entries.
 */
@Service
public class PlanEntryService {

    private PlanEntryRepository planEntryRepository;
    private TeamRepository teamRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    /**
     * Constructs a PlanEntryService with the specified dependencies.
     *
     * @param planEntryRepository the repository for plan entry operations
     * @param teamRepository      the repository for team operations
     * @param userRepository      the repository for user operations
     * @param modelMapper         the model mapper for DTO conversions
     */
    public PlanEntryService(
            PlanEntryRepository planEntryRepository,
            TeamRepository teamRepository,
            UserRepository userRepository,
            ModelMapper modelMapper
    ) {
        this.planEntryRepository = planEntryRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Retrieves all plan entries.
     *
     * @return a list of PlanEntryDto representing all plan entries
     */
    public List<PlanEntryDto> retrieveAllPlanEntries() {
        List<PlanEntry> planEntries = planEntryRepository.findAll();
        return planEntries.stream().map(planEntry -> modelMapper.map(planEntry, PlanEntryDto.class)).toList();
    }

    /**
     * Retrieves a plan entry by ID.
     *
     * @param planEntryId the ID of the plan entry
     * @return a PlanEntryDto representing the found plan entry
     * @throws PlanEntryNotFoundException if the plan entry with the given ID does not exist
     */
    public PlanEntryDto retrievePlanEntryById(int planEntryId) {
        PlanEntry planEntry = planEntryRepository.findById(planEntryId).orElseThrow(PlanEntryNotFoundException::new);
        return modelMapper.map(planEntry, PlanEntryDto.class);
    }

    /**
     * Retrieves plan entries associated with a specific team and user.
     *
     * @param teamId the ID of the team
     * @param userId the ID of the user
     * @return a list of PlanEntryDto representing the found plan entries
     * @throws TeamNotFoundException if the team with the given ID does not exist
     * @throws UserNotFoundException if the user with the given ID does not exist
     */
    public List<PlanEntryDto> retrievePlanEntriesByTeamIdAndUserId(int teamId, int userId) {
        teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new);
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        List<PlanEntry> planEntries = planEntryRepository.findPlanEntriesByTeamIdAndUserId(teamId, userId);
        return planEntries.stream()
                .map(planEntry -> modelMapper.map(planEntry, PlanEntryDto.class))
                .toList();
    }

    /**
     * Creates a new plan entry associated with a specific team and user.
     *
     * @param planEntryDto the creation data for the new plan entry
     * @param teamId       the ID of the team associated with the plan entry
     * @param userId       the ID of the user associated with the plan entry
     * @throws TeamNotFoundException if the team with the given ID does not exist
     * @throws UserNotFoundException if the user with the given ID does not exist
     */
    public void createPlanEntry(
            PlanEntryDto planEntryDto,
            int teamId,
            int userId
    ) {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        PlanEntry planEntry = modelMapper.map(planEntryDto, PlanEntry.class);
        planEntry.setTeam(team);
        planEntry.setUser(user);
        planEntryRepository.save(planEntry);
    }

    /**
     * Deletes a plan entry by ID.
     *
     * @param planEntryId the ID of the plan entry to be deleted
     */
    public void deletePlanEntryById(int planEntryId) {
        planEntryRepository.deleteById(planEntryId);
    }
}
