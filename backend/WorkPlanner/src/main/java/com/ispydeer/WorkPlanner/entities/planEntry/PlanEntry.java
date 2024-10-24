package com.ispydeer.WorkPlanner.entities.planEntry;

import com.ispydeer.WorkPlanner.entities.planEntry.planEntryColor.PlanEntryColor;
import com.ispydeer.WorkPlanner.entities.team.Team;
import com.ispydeer.WorkPlanner.entities.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a plan entry, which includes details about
 * a scheduled task or event, associated with a specific user and team.
 */
@Entity
@Data
@NoArgsConstructor
public class PlanEntry {

    /**
     * The id of the plan entry.
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * The title of the plan entry.
     */
    private String title;

    /**
     * The start time of the plan entry.
     * Must not be null.
     */
    @NotNull
    private LocalDateTime startTime;

    /**
     * The end time of the plan entry.
     * Must not be null.
     */
    @NotNull
    private LocalDateTime endTime;

    /**
     * The user associated with this plan entry.
     * Must not be null.
     */
    @NotNull
    @ManyToOne
    private User user;

    /**
     * The team associated with this plan entry.
     * Must not be null.
     */
    @NotNull
    @ManyToOne
    private Team team;

    /**
     * The color of plan entry's graphical representation.
     * Must not be null and is persisted as a string.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    private PlanEntryColor planEntryColor;
}
