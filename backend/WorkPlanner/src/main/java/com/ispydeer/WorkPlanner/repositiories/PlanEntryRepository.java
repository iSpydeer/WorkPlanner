package com.ispydeer.WorkPlanner.repositiories;

import com.ispydeer.WorkPlanner.entities.planEntry.PlanEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for accessing and managing PlanEntry entities.
 */
public interface PlanEntryRepository extends JpaRepository<PlanEntry, Integer> {

    /**
     * Finds all plan entries associated with a specific team and user.
     *
     * @param teamId the ID of the team
     * @param userId the ID of the user
     * @return a list of plan entries that belong to the specified team and user
     */
    List<PlanEntry> findPlanEntriesByTeamIdAndUserId(int teamId, int userId);
}
