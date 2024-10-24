package com.ispydeer.WorkPlanner.controllers;

import com.ispydeer.WorkPlanner.entities.planEntry.dto.PlanEntryDto;
import com.ispydeer.WorkPlanner.services.PlanEntryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlanEntryController {

    private final PlanEntryService planEntryService;

    public PlanEntryController(PlanEntryService planEntryService) {
        this.planEntryService = planEntryService;
    }

    /**
     * Retrieves all plan entries
     *
     * @return a list of PlanEntryDto objects and HTTP 200 OK status.
     */
    @GetMapping("/plan-entries")
    public ResponseEntity<List<PlanEntryDto>> retrieveAllPlanEntries() {
        List<PlanEntryDto> planEntries = planEntryService.retrieveAllPlanEntries();
        return new ResponseEntity<>(planEntries, HttpStatus.OK);
    }

    /**
     * Retrieves a specific plan entry by its ID.
     *
     * @param planEntryId the ID of the plan entry to retrieve.
     * @return the PlanEntryDto object corresponding to the given ID and HTTP 200 OK status.
     */
    @GetMapping("/plan-entries/{planEntryId}")
    public ResponseEntity<PlanEntryDto> retrievePlanEntry(@PathVariable Integer planEntryId) {
        PlanEntryDto planEntryDto = planEntryService.retrievePlanEntryById(planEntryId);
        return new ResponseEntity<>(planEntryDto, HttpStatus.OK);
    }

    /**
     * Retrieves plan entries for a specific team and user.
     *
     * @param teamId the ID of the team.
     * @param userId the ID of the user.
     * @return a list of PlanEntryDto objects filtered by teamId and userId, and HTTP 200 OK status.
     */
    @GetMapping("/plan-entries/teams/{teamId}/users/{userId}")
    public ResponseEntity<List<PlanEntryDto>> retrievePlanEntriesByTeamAndUser(
            @PathVariable Integer teamId,
            @PathVariable Integer userId
    ) {
        List<PlanEntryDto> planEntries = planEntryService.retrievePlanEntriesByTeamIdAndUserId(teamId, userId);
        return new ResponseEntity<>(planEntries, HttpStatus.OK);
    }

    /**
     * Creates a new plan entry associated with specific team and user.
     *
     * @param planEntryDto the DTO containing plan entry details.
     * @param teamId       the ID of the team for which the plan entry is being created.
     * @param userId       the ID of the user associated with the plan entry.
     * @return HTTP 201 CREATED status.
     */
    @PostMapping("/plan-entries/teams/{teamId}/users/{userId}")
    public ResponseEntity<PlanEntryDto> createPlanEntry(
            @Valid @RequestBody PlanEntryDto planEntryDto,
            @PathVariable Integer teamId,
            @PathVariable Integer userId
    ) {
        planEntryService.createPlanEntry(planEntryDto, teamId, userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Deletes a plan entry by its ID.
     *
     * @param planEntryId the ID of the plan entry to delete
     * @return HTTP 204 NO CONTENT status
     */
    @DeleteMapping("/plan-entries/{planEntryId}")
    public ResponseEntity<PlanEntryDto> deletePlanEntry(@PathVariable Integer planEntryId) {
        planEntryService.deletePlanEntryById(planEntryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
