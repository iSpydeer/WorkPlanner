package com.ispydeer.WorkPlanner.entities.team;

import com.ispydeer.WorkPlanner.entities.planEntry.PlanEntry;
import com.ispydeer.WorkPlanner.entities.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a team, which can consist of multiple users and is
 * associated with various plan entries.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Team {

    /**
     * The id of the team.
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * The name of the team.
     */
    private String name;

    /**
     * A description of the team.
     */
    private String description;

    /**
     * The date when the team was created.
     */
    private LocalDateTime teamCreationDate;

    /**
     * A set of users that are part of this team.
     */
    @ManyToMany
    private Set<User> setOfUsers = new HashSet<>();

    /**
     * The user designated as the team leader.
     */
    @ManyToOne
    private User teamLeader;

    /**
     * A set of plan entries associated with this team.
     */
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PlanEntry> setOfPlanEntries = new HashSet<>();

    /**
     * Constructs a new Team with the specified name, first name, and description.
     *
     * @param name  the name of the team
     * @param description the description of the team
     */
    public Team(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
