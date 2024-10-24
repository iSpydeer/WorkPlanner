package com.ispydeer.WorkPlanner.services;

import com.ispydeer.WorkPlanner.controllers.exceptions.UserNotFoundException;
import com.ispydeer.WorkPlanner.entities.user.User;
import com.ispydeer.WorkPlanner.repositiories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for handling user authentication details.
 */
@Service
public class AuthUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructs an AuthUserDetailsService with the specified user repository.
     *
     * @param userRepository the repository for user operations
     */
    public AuthUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user details by username for authentication.
     *
     * @param username the username of the user to be loaded
     * @return UserDetails containing the user's information for authentication
     * @throws UsernameNotFoundException if the user with the given username does not exist
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.orElseThrow(UserNotFoundException::new);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
