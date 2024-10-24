package com.ispydeer.WorkPlanner.entities.planEntry.dto;

import com.ispydeer.WorkPlanner.entities.planEntry.planEntryColor.PlanEntryColor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for transferring plan entry data between layers
 * of the application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanEntryDto {

    /**
     * The id of the plan entry.
     */
    private Integer id;

    /**
     * The title of the plan entry.
     */
    @Size(min = 4, max = 20, message = "PlanEntry title must contain min. 4 characters and max. 20 characters")
    private String title;

    /**
     * The start time of the plan entry.
     */
    @NotNull
    private LocalDateTime startTime;

    /**
     * The end time of the plan entry.
     */
    @NotNull
    private LocalDateTime endTime;

    /**
     * The color of plan entry's graphical representation.
     */
    private PlanEntryColor planEntryColor;
}
