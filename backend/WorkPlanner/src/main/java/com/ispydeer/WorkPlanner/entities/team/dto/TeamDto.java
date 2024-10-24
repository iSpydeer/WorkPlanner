package com.ispydeer.WorkPlanner.entities.team.dto;

import com.ispydeer.WorkPlanner.entities.user.dto.UserDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for transferring team data between layers
 * of the application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDto {

    /**
     * The id of the team.
     */
    private Integer id;

    /**
     * The name of the team.
     */
    @Size(min = 5, max = 20, message = "Team name must contain min. 5 characters and max. 20 characters")
    private String name;

    /**
     * A description of the team.
     */
    @NotNull
    @Size(max = 30, message = "Description must contain min. 5 characters and max. 30 characters")
    private String description;

    /**
     * The user designated as the team leader.
     */
    private UserDto teamLeader;

}
