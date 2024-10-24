package com.ispydeer.WorkPlanner.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ispydeer.WorkPlanner.entities.planEntry.dto.PlanEntryDto;
import com.ispydeer.WorkPlanner.entities.team.dto.TeamDto;
import com.ispydeer.WorkPlanner.entities.user.dto.UserRegistrationDto;
import com.ispydeer.WorkPlanner.services.PlanEntryService;
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
public class PlanEntryControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlanEntryService planEntryService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    @WithMockUser
    public void testThatCreatesPlanEntrySuccessfullyAndReturnsHttpStatus201() throws Exception {
        UserRegistrationDto userDtoRegA = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userDtoRegA);

        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        teamService.createTeam(teamDtoA);

        Integer userId = userService.retrieveUserByUsername(userDtoRegA.getUsername()).getId();
        Integer teamId = teamService.retrieveTeamByName(teamDtoA.getName()).getId();

        PlanEntryDto planEntryDtoA = TestDataCreator.createPlanEntryDtoA();

        String json = mapper.writeValueAsString(planEntryDtoA);

        mockMvc.perform(MockMvcRequestBuilders.post("/plan-entries/teams/" + teamId + "/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser
    public void testThatGetsPlanEntrySuccessfullyAndReturnsHttpStatus200() throws Exception {
        UserRegistrationDto userDtoRegA = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userDtoRegA);

        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        teamService.createTeam(teamDtoA);

        Integer userId = userService.retrieveUserByUsername(userDtoRegA.getUsername()).getId();
        Integer teamId = teamService.retrieveTeamByName(teamDtoA.getName()).getId();

        PlanEntryDto planEntryDtoA = TestDataCreator.createPlanEntryDtoA();
        planEntryService.createPlanEntry(planEntryDtoA, teamId, userId);

        Integer planEntryId = planEntryService.retrieveAllPlanEntries().getFirst().getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/plan-entries/" + planEntryId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(planEntryId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(planEntryDtoA.getTitle()));
    }

    @Test
    @WithMockUser
    public void testThatGetsPlanEntryUnsuccessfullyAndReturnsHttpStatus404() throws Exception {
        final int ID_OF_NOT_EXISTING_TEAM = 999;

        mockMvc.perform(MockMvcRequestBuilders.get("/plan-entries/" + ID_OF_NOT_EXISTING_TEAM))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testThatListsPlanEntriesSuccessfullyAndReturnsHttpStatus200() throws Exception {
        UserRegistrationDto userDtoRegA = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userDtoRegA);

        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        teamService.createTeam(teamDtoA);

        Integer userId = userService.retrieveUserByUsername(userDtoRegA.getUsername()).getId();
        Integer teamId = teamService.retrieveTeamByName(teamDtoA.getName()).getId();

        PlanEntryDto planEntryDtoA = TestDataCreator.createPlanEntryDtoA();
        planEntryService.createPlanEntry(planEntryDtoA, teamId, userId);
        PlanEntryDto planEntryDtoB = TestDataCreator.createPlanEntryDtoB();
        planEntryService.createPlanEntry(planEntryDtoB, teamId, userId);

        mockMvc.perform(MockMvcRequestBuilders.get("/plan-entries"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].title",
                        Matchers.containsInAnyOrder(planEntryDtoA.getTitle(), planEntryDtoB.getTitle())));

    }

    @Test
    @WithMockUser
    public void testThatDeletesPlanEntrySuccessfullyAndReturnsHttpStatus204() throws Exception {
        UserRegistrationDto userDtoRegA = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userDtoRegA);

        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        teamService.createTeam(teamDtoA);

        Integer userId = userService.retrieveUserByUsername(userDtoRegA.getUsername()).getId();
        Integer teamId = teamService.retrieveTeamByName(teamDtoA.getName()).getId();

        PlanEntryDto planEntryDtoA = TestDataCreator.createPlanEntryDtoA();
        planEntryService.createPlanEntry(planEntryDtoA, teamId, userId);

        Integer planEntryId = planEntryService.retrieveAllPlanEntries().getFirst().getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/plan-entries/" + planEntryId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
