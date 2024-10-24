package com.ispydeer.WorkPlanner.entities.user.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for transferring user registration data between layers
 * of the application.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto extends UserDto {

    /**
     * The password of the user.
     */
    @Size(min = 3, max = 15, message = "Password must contain min. 3 characters and max. 15 characters")
    private String password;
}
