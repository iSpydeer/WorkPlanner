package com.ispydeer.WorkPlanner.security.jwt;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtAuthenticationController {

    private final JwtTokenService jwtTokenService;

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationController(JwtTokenService jwtTokenService, AuthenticationManager authenticationManager) {
        this.jwtTokenService = jwtTokenService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Generates a JWT token based on the provided authentication request.
     *
     * @param jwtTokenRequest the authentication request containing username and password
     * @return a JWT token and HTTP 200 OK status.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<JwtTokenResponse> generateToken(
            @RequestBody JwtTokenRequest jwtTokenRequest
    ) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                jwtTokenRequest.username(),
                jwtTokenRequest.password()
        );

        Authentication authentication = authenticationManager.authenticate(authToken);
        String token = jwtTokenService.generateToken(authentication);
        return ResponseEntity.ok(new JwtTokenResponse(token));
    }

}
