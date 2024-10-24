package com.ispydeer.WorkPlanner.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ispydeer.WorkPlanner.entities.team.dto.TeamDto;
import com.ispydeer.WorkPlanner.entities.user.dto.UserRegistrationDto;
import com.ispydeer.WorkPlanner.services.TeamService;
import com.ispydeer.WorkPlanner.services.UserService;
import com.ispydeer.WorkPlanner.utilities.TestDataCreator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@Transactional
public class UserControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser
    public void testThatCreatesUserSuccessfullyReturnsHttpStatus201() throws Exception {
        UserRegistrationDto userDtoRegA = TestDataCreator.createUserRegistrationDtoA();
        String json = objectMapper.writeValueAsString(userDtoRegA);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser
    public void testThatGetsUserSuccessfullyReturnsHttpStatus200() throws Exception {
        UserRegistrationDto userDtoRegA = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userDtoRegA);
        Integer userId = userService.retrieveUserByUsername(userDtoRegA.getUsername()).getId();

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/" + userId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(userDtoRegA.getFirstName()));
    }

    @Test
    @WithMockUser
    public void testThatGetsUserUnsuccessfullyReturnsHttpStatus404() throws Exception {
        UserRegistrationDto userDtoRegA = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userDtoRegA);
        final int ID_OF_NOT_EXISTING_USER = 999;

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/" + ID_OF_NOT_EXISTING_USER))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testThatListsUsersSuccessfullyReturnsHttpStatus200() throws Exception {
        UserRegistrationDto userDtoRegA = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userDtoRegA);
        UserRegistrationDto userDtoRegB = TestDataCreator.createUserRegistrationDtoB();
        userService.createUser(userDtoRegB);

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].firstName",
                        Matchers.containsInAnyOrder(
                                userDtoRegA.getFirstName(),
                                userDtoRegB.getFirstName())));
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_ADMIN"})
    public void testThatDeletesUserReturnsHttpStatus204() throws Exception {
        UserRegistrationDto userDtoRegA = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userDtoRegA);

        Integer userId = userService.retrieveUserByUsername(userDtoRegA.getUsername()).getId();
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/users/" + userId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser
    public void testThatAddsTeamToUserSuccessfullyReturnsHttpStatus201() throws Exception {
        UserRegistrationDto userDtoRegA = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userDtoRegA);

        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        teamService.createTeam(teamDtoA);

        Integer userId = userService.retrieveUserByUsername(userDtoRegA.getUsername()).getId();
        Integer teamId = teamService.retrieveTeamByName(teamDtoA.getName()).getId();

        mockMvc.perform(MockMvcRequestBuilders.put("/users/" + userId + "/teams/" + teamId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testThatListsTeamsSuccessfullyReturnsHttpStatus200() throws Exception {
        UserRegistrationDto userDtoRegA = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userDtoRegA);

        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        teamService.createTeam(teamDtoA);

        TeamDto teamDtoB = TestDataCreator.createTeamDtoB();
        teamService.createTeam(teamDtoB);

        Integer userId = userService.retrieveUserByUsername(userDtoRegA.getUsername()).getId();
        Integer teamIdA = teamService.retrieveTeamByName(teamDtoA.getName()).getId();
        Integer teamIdB = teamService.retrieveTeamByName(teamDtoB.getName()).getId();

        userService.addUserTeam(userId, teamIdB);
        userService.addUserTeam(userId, teamIdA);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + userId + "/teams"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id",
                        Matchers.containsInAnyOrder(
                                teamIdA,
                                teamIdB)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name",
                        Matchers.containsInAnyOrder(
                                teamDtoA.getName(),
                                teamDtoB.getName())));
    }

    @Test
    @WithMockUser
    public void testThatDeletesTeamFromUserSuccessfullyReturnsHttpStatus204() throws Exception {
        UserRegistrationDto userDtoRegA = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userDtoRegA);

        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        teamService.createTeam(teamDtoA);

        Integer userId = userService.retrieveUserByUsername(userDtoRegA.getUsername()).getId();
        Integer teamId = teamService.retrieveTeamByName(teamDtoA.getName()).getId();
        userService.addUserTeam(userId, teamId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + userId + "/teams/" + teamId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
