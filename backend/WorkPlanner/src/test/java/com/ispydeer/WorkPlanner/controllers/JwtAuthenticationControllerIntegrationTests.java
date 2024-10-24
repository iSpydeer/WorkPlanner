package com.ispydeer.WorkPlanner.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ispydeer.WorkPlanner.entities.user.dto.UserRegistrationDto;
import com.ispydeer.WorkPlanner.security.jwt.JwtTokenRequest;
import com.ispydeer.WorkPlanner.services.UserService;
import com.ispydeer.WorkPlanner.utilities.TestDataCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class JwtAuthenticationControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testThatGeneratesJwtTokenSuccessfullyAndReturnsStatus200() throws Exception {
        UserRegistrationDto userRegistrationDtoA = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userRegistrationDtoA);

        JwtTokenRequest request = new JwtTokenRequest(
                userRegistrationDtoA.getUsername(),
                userRegistrationDtoA.getPassword());

        String json = mapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}
