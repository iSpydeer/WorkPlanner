package com.ispydeer.WorkPlanner.repositiories;

import com.ispydeer.WorkPlanner.entities.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for accessing and managing Team entities.
 */
public interface TeamRepository extends JpaRepository<Team, Integer> {

    /**
     * Finds a team by its name.
     *
     * @param name the name of the team
     * @return an Optional containing the found team, or empty if no team with the given name exists
     */
    Optional<Team> findByName(String name);

    /**
     * Checks if a team exists with the given name.
     *
     * @param name the name of the team
     * @return true if a team with the specified name exists, false otherwise
     */
    boolean existsByName(String name);
}
