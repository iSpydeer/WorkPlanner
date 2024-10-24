package com.ispydeer.WorkPlanner.repositiories;

import com.ispydeer.WorkPlanner.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for accessing and managing User entities.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by its username.
     *
     * @param username the username of the user
     * @return an Optional containing the found user, or empty if no user with the given username exists
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user exists with the given username.
     *
     * @param username the name of the user
     * @return true if a user with the specified username exists, false otherwise
     */
    boolean existsByUsername(String username);
}
