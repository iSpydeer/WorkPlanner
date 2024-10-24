package com.ispydeer.WorkPlanner.security.jwt;

/**
 * A record to hold the JWT token response.
 *
 * @param token the JWT token string
 */
public record JwtTokenResponse(String token) {
}
