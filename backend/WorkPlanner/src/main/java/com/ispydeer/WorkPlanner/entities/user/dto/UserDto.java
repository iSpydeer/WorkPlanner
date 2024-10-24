package com.ispydeer.WorkPlanner.entities.user.dto;

import com.ispydeer.WorkPlanner.entities.user.role.Role;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for transferring user data between layers
 * of the application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    /**
     * The id of the user.
     */
    private Integer id;

    /**
     * The username of the user.
     */
    @Size(min = 4, max = 20, message = "Username must contain min. 4 characters and max. 20 characters")
    private String username;

    /**
     * The first name of the user.
     */
    @Size(min = 2, max = 20, message = "First name must contain min. 4 characters and max. 20 characters")
    private String firstName;

    /**
     * The last name of the user.
     */
    @Size(min = 2, max = 20, message = "Last name must contain min. 4 characters and max. 20 characters")
    private String lastName;

    /**
     * The role of the user.
     */
    private Role role;
}
