package com.ispydeer.WorkPlanner.security.jwt;

import com.ispydeer.WorkPlanner.entities.user.dto.UserDto;
import com.ispydeer.WorkPlanner.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Service for generating JWT tokens for authenticated users.
 */
@Service
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;
    private final UserService userService;

    public JwtTokenService(JwtEncoder jwtEncoder, UserService userService) {
        this.jwtEncoder = jwtEncoder;
        this.userService = userService;
    }

    /**
     * Generates a JWT token for the authenticated user.
     *
     * @param authentication the authentication object containing user details
     * @return the generated JWT token as a string
     */
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        UserDto user = userService.retrieveUserByUsername(username);

        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(120, ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim("id", user.getId())
                .claim("scope", user.getRole().name())
                .build();

        return this.jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }
}
