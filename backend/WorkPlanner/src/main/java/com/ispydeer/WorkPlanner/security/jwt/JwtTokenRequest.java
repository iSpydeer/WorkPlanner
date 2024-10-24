package com.ispydeer.WorkPlanner.security.jwt;

/**
 * A record to hold the JWT token request details.
 *
 * @param username the username for authentication
 * @param password the password for authentication
 */
public record JwtTokenRequest(String username, String password) {
}
