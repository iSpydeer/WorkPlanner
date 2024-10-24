package com.ispydeer.WorkPlanner.services;

import com.ispydeer.WorkPlanner.controllers.exceptions.PlanEntryNotFoundException;
import com.ispydeer.WorkPlanner.entities.planEntry.dto.PlanEntryDto;
import com.ispydeer.WorkPlanner.entities.team.dto.TeamDto;
import com.ispydeer.WorkPlanner.entities.user.dto.UserRegistrationDto;
import com.ispydeer.WorkPlanner.utilities.TestDataCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class PlanEntryServiceIntegrationTests {

    @Autowired
    private PlanEntryService underTest;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    @Test
    public void testThatPlanEntryIsSuccessfullySavedAndRecalledById() {
        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        teamService.createTeam(teamDtoA);
        UserRegistrationDto userRegistrationDto = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userRegistrationDto);

        int userId = userService.retrieveUserByUsername(userRegistrationDto.getUsername()).getId();
        int teamId = teamService.retrieveTeamByName(teamDtoA.getName()).getId();

        PlanEntryDto planEntryDtoA = TestDataCreator.createPlanEntryDtoA();
        underTest.createPlanEntry(planEntryDtoA, teamId, userId);

        int planEntryId = underTest.retrieveAllPlanEntries().getFirst().getId();
        PlanEntryDto result = underTest.retrievePlanEntryById(planEntryId);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(planEntryDtoA.getTitle());
    }

    @Test
    public void testThatPlanEntryIsSuccessfullySavedAndRecalledByTeamIdAndUserId() {
        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        teamService.createTeam(teamDtoA);
        UserRegistrationDto userRegistrationDto = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userRegistrationDto);

        int userId = userService.retrieveUserByUsername(userRegistrationDto.getUsername()).getId();
        int teamId = teamService.retrieveTeamByName(teamDtoA.getName()).getId();

        PlanEntryDto planEntryDtoA = TestDataCreator.createPlanEntryDtoA();
        underTest.createPlanEntry(planEntryDtoA, teamId, userId);

        PlanEntryDto result = underTest.retrievePlanEntriesByTeamIdAndUserId(teamId, userId).getFirst();

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(planEntryDtoA.getTitle());
    }

    @Test
    public void testThatPlanEntryIsNotFoundById() {
        final int ID_OF_NON_EXISTING_PLAN_ENTRY = 9999;
        assertThatThrownBy(() -> underTest.retrievePlanEntryById(ID_OF_NON_EXISTING_PLAN_ENTRY))
                .isInstanceOf(PlanEntryNotFoundException.class);
    }

    @Test
    public void testThatPlanEntriesAreSuccessfullySavedAndRecalled() {
        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        teamService.createTeam(teamDtoA);
        UserRegistrationDto userRegistrationDto = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userRegistrationDto);

        int userId = userService.retrieveUserByUsername(userRegistrationDto.getUsername()).getId();
        int teamId = teamService.retrieveTeamByName(teamDtoA.getName()).getId();

        PlanEntryDto planEntryDtoA = TestDataCreator.createPlanEntryDtoA();
        underTest.createPlanEntry(planEntryDtoA, teamId, userId);
        PlanEntryDto planEntryDtoB = TestDataCreator.createPlanEntryDtoB();
        underTest.createPlanEntry(planEntryDtoB, teamId, userId);

        List<PlanEntryDto> result = underTest.retrieveAllPlanEntries();
        assertThat(result)
                .hasSize(2)
                .extracting(PlanEntryDto::getTitle)
                .containsExactlyInAnyOrder(
                        planEntryDtoA.getTitle(),
                        planEntryDtoB.getTitle()
                );
    }

    @Test
    public void testThatPlanEntryIsSuccessfullyDeleted() {
        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        teamService.createTeam(teamDtoA);
        UserRegistrationDto userRegistrationDto = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userRegistrationDto);

        int userId = userService.retrieveUserByUsername(userRegistrationDto.getUsername()).getId();
        int teamId = teamService.retrieveTeamByName(teamDtoA.getName()).getId();

        PlanEntryDto planEntryDtoA = TestDataCreator.createPlanEntryDtoA();
        underTest.createPlanEntry(planEntryDtoA, teamId, userId);
        PlanEntryDto planEntryDtoB = TestDataCreator.createPlanEntryDtoB();
        underTest.createPlanEntry(planEntryDtoB, teamId, userId);

        int planEntryId = underTest
                .retrieveAllPlanEntries()
                .stream()
                .filter(planEntry -> planEntry.getTitle().equals(planEntryDtoA.getTitle()))
                .findFirst()
                .get()
                .getId();

        System.out.println(planEntryId);
        System.out.println(underTest.retrievePlanEntryById(planEntryId).getTitle());

        underTest.deletePlanEntryById(planEntryId);

        List<PlanEntryDto> result = underTest.retrieveAllPlanEntries();
        assertThat(result)
                .hasSize(1)
                .extracting(PlanEntryDto::getTitle)
                .containsExactlyInAnyOrder(
                        planEntryDtoB.getTitle()
                );

    }
}
