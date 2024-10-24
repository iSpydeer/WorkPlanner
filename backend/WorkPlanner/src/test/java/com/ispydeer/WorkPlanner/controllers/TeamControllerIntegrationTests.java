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

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@Transactional
public class TeamControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @WithMockUser(authorities = {"SCOPE_ADMIN"})
    public void testThatCreatesTeamSuccessfullyAndReturnsHttpStatus201() throws Exception {
        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        String json = mapper.writeValueAsString(teamDtoA);

        mockMvc.perform(MockMvcRequestBuilders.post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser
    public void testThatGetsTeamSuccessfullyAndReturnsHttpStatus200() throws Exception {
        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        teamService.createTeam(teamDtoA);
        Integer teamId = teamService.retrieveTeamByName(teamDtoA.getName()).getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/teams/" + teamId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(teamId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(teamDtoA.getName()));
    }

    @Test
    @WithMockUser
    public void testThatGetsTeamUnsuccessfullyAndReturnsHttpStatus404() throws Exception {
        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        teamService.createTeam(teamDtoA);
        final int ID_OF_NOT_EXISTING_TEAM = 999;

        mockMvc.perform(MockMvcRequestBuilders.get("/teams/" + ID_OF_NOT_EXISTING_TEAM))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testThatListsTeamsSuccessfullyAndReturnsHttpStatus200() throws Exception {
        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        teamService.createTeam(teamDtoA);
        TeamDto teamDtoB = TestDataCreator.createTeamDtoB();
        teamService.createTeam(teamDtoB);

        mockMvc.perform(MockMvcRequestBuilders.get("/teams"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name",
                        Matchers.containsInAnyOrder(teamDtoA.getName(), teamDtoB.getName())));
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_ADMIN"})
    public void testThatDeletesTeamSuccessfullyAndReturnsHttpStatus204() throws Exception {
        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        teamService.createTeam(teamDtoA);
        Integer teamId = teamService.retrieveTeamByName(teamDtoA.getName()).getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/teams/" + teamId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_ADMIN"})
    public void testThatAssignsTeamLeaderSuccessfullyAndReturnsHttpStatus200() throws Exception {
        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        teamService.createTeam(teamDtoA);
        Integer teamId = teamService.retrieveTeamByName(teamDtoA.getName()).getId();

        UserRegistrationDto userRegistrationDtoA = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userRegistrationDtoA);
        Integer userId = userService.retrieveUserByUsername(userRegistrationDtoA.getUsername()).getId();

        mockMvc.perform(MockMvcRequestBuilders.put("/teams/" + teamId + "/team-leader/" + userId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_ADMIN"})
    public void testThatUnassignsTeamLeaderSuccessfullyAndReturnsHttpStatus204() throws Exception {
        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        teamService.createTeam(teamDtoA);
        Integer teamId = teamService.retrieveTeamByName(teamDtoA.getName()).getId();

        UserRegistrationDto userRegistrationDtoA = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userRegistrationDtoA);
        Integer userId = userService.retrieveUserByUsername(userRegistrationDtoA.getUsername()).getId();

        teamService.setTeamLeader(teamId, userId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/teams/" + teamId + "/team-leader"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser
    public void testThatAddsUsersToTeamSuccessfullyReturnsHttpStatus200() throws Exception {
        UserRegistrationDto userRegistrationDtoA = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userRegistrationDtoA);
        UserRegistrationDto userRegistrationDtoB = TestDataCreator.createUserRegistrationDtoB();
        userService.createUser(userRegistrationDtoB);

        Integer userAId = userService.retrieveUserByUsername(userRegistrationDtoA.getUsername()).getId();
        Integer userBId = userService.retrieveUserByUsername(userRegistrationDtoB.getUsername()).getId();
        List<Integer> idList = Arrays.asList(userAId, userBId);

        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        teamService.createTeam(teamDtoA);
        Integer teamId = teamService.retrieveTeamByName(teamDtoA.getName()).getId();

        String json = mapper.writeValueAsString(idList);

        mockMvc.perform(MockMvcRequestBuilders.put("/teams/" + teamId + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testThatListsTeamUsersSuccessfullyAndReturnsStatus200() throws Exception {
        UserRegistrationDto userRegistrationDtoA = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userRegistrationDtoA);
        UserRegistrationDto userRegistrationDtoB = TestDataCreator.createUserRegistrationDtoB();
        userService.createUser(userRegistrationDtoB);

        Integer userAId = userService.retrieveUserByUsername(userRegistrationDtoA.getUsername()).getId();
        Integer userBId = userService.retrieveUserByUsername(userRegistrationDtoB.getUsername()).getId();
        List<Integer> idList = Arrays.asList(userAId, userBId);

        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        teamService.createTeam(teamDtoA);
        Integer teamId = teamService.retrieveTeamByName(teamDtoA.getName()).getId();

        teamService.addTeamUsers(teamId, idList);

        mockMvc.perform(MockMvcRequestBuilders.get("/teams/" + teamId + "/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].firstName",
                        Matchers.containsInAnyOrder(
                                userRegistrationDtoA.getFirstName(),
                                userRegistrationDtoB.getFirstName()
                        )));
    }

    @Test
    @WithMockUser
    public void testThatRemovesTeamUserSuccessfullyAndReturnsStatus200() throws Exception {
        UserRegistrationDto userRegistrationDtoA = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userRegistrationDtoA);

        Integer userAId = userService.retrieveUserByUsername(userRegistrationDtoA.getUsername()).getId();
        List<Integer> idList = Arrays.asList(userAId);

        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        teamService.createTeam(teamDtoA);
        Integer teamId = teamService.retrieveTeamByName(teamDtoA.getName()).getId();

        teamService.addTeamUsers(teamId, idList);

        mockMvc.perform(MockMvcRequestBuilders.delete("/teams/" + teamId + "/users/" + userAId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
