package com.ispydeer.WorkPlanner.services;

import com.ispydeer.WorkPlanner.controllers.exceptions.TeamNotFoundException;
import com.ispydeer.WorkPlanner.entities.team.dto.TeamDto;
import com.ispydeer.WorkPlanner.entities.user.dto.UserDto;
import com.ispydeer.WorkPlanner.entities.user.dto.UserRegistrationDto;
import com.ispydeer.WorkPlanner.utilities.TestDataCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class TeamServiceIntegrationTests {

    @Autowired
    private TeamService underTest;

    @Autowired
    private UserService userService;

    @Test
    public void testThatTeamIsSuccessfullySavedAndRecalledByName() {
        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        underTest.createTeam(teamDtoA);

        TeamDto result = underTest.retrieveTeamByName(teamDtoA.getName());
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(teamDtoA.getName());
    }

    @Test
    public void testThatTeamIsNotFoundByName() {
        assertThatThrownBy(() -> underTest.retrieveTeamByName("NonExistentTeam"))
                .isInstanceOf(TeamNotFoundException.class);
    }

    @Test
    public void testThatTeamIsSuccessfullySavedAndRecalledById() {
        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        underTest.createTeam(teamDtoA);

        int teamId = underTest.retrieveTeamByName(teamDtoA.getName()).getId();
        TeamDto result = underTest.retrieveTeamById(teamId);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(teamDtoA.getName());
    }

    @Test
    public void testThatTeamIsNotFoundById() {
        final int ID_OF_NON_EXISTING_TEAM = 9999;
        assertThatThrownBy(() -> underTest.retrieveTeamById(ID_OF_NON_EXISTING_TEAM))
                .isInstanceOf(TeamNotFoundException.class);
    }

    @Test
    public void testThatTeamsAreSuccessfullySavedAndRecalled() {
        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        TeamDto teamDtoB = TestDataCreator.createTeamDtoB();
        underTest.createTeam(teamDtoA);
        underTest.createTeam(teamDtoB);

        List<TeamDto> result = underTest.retrieveAllTeams();
        assertThat(result)
                .hasSize(2)
                .extracting(TeamDto::getName)
                .containsExactlyInAnyOrder(
                        teamDtoA.getName(),
                        teamDtoB.getName()
                );
    }

    @Test
    public void testThatTeamIsSuccessfullyDeleted() {
        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        underTest.createTeam(teamDtoA);

        int teamId = underTest.retrieveTeamByName(teamDtoA.getName()).getId();
        underTest.deleteTeam(teamId);
        assertThatThrownBy(
                () -> underTest.retrieveTeamById(teamId)
        ).isInstanceOf(TeamNotFoundException.class);
    }

    @Test
    public void testThatTeamLeaderCanBeSuccessfullyAssignedToTeam() {
        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        underTest.createTeam(teamDtoA);
        UserRegistrationDto userRegistrationDto = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userRegistrationDto);

        int userId = userService.retrieveUserByUsername(userRegistrationDto.getUsername()).getId();
        int teamId = underTest.retrieveTeamByName(teamDtoA.getName()).getId();

        underTest.setTeamLeader(teamId, userId);
        UserDto teamLeader = userService.retrieveUserById(userId);
        TeamDto result = underTest.retrieveTeamById(teamId);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(teamDtoA.getName());
        assertThat(result.getTeamLeader()).isEqualTo(teamLeader);
    }

    @Test
    public void testThatTeamLeaderCanBeSuccessfullyUnassignedFromTeam() {
        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        underTest.createTeam(teamDtoA);
        UserRegistrationDto userRegistrationDto = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userRegistrationDto);

        Integer userId = userService.retrieveUserByUsername(userRegistrationDto.getUsername()).getId();
        Integer teamId = underTest.retrieveTeamByName(teamDtoA.getName()).getId();

        underTest.setTeamLeader(teamId, userId);
        underTest.resetTeamLeader(teamId);

        TeamDto result = underTest.retrieveTeamById(teamId);
        assertThat(result).isNotNull();
        assertThat(result.getTeamLeader()).isNull();
    }

    @Test
    public void testThatUsersCanBeSuccessfullyAssignedToTeamAndRecalled() {
        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        underTest.createTeam(teamDtoA);

        UserRegistrationDto userRegistrationDtoA = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userRegistrationDtoA);
        UserRegistrationDto userRegistrationDtoB = TestDataCreator.createUserRegistrationDtoB();
        userService.createUser(userRegistrationDtoB);

        Integer userAId = userService.retrieveUserByUsername(userRegistrationDtoA.getUsername()).getId();
        Integer userBId = userService.retrieveUserByUsername(userRegistrationDtoB.getUsername()).getId();
        Integer teamId = underTest.retrieveTeamByName(teamDtoA.getName()).getId();

        underTest.addTeamUsers(teamId, Arrays.asList(userAId, userBId));
        List<UserDto> result = underTest.retrieveTeamUsers(teamId);

        assertThat(result)
                .hasSize(2)
                .extracting(UserDto::getUsername)
                .containsExactlyInAnyOrder(
                        userRegistrationDtoA.getUsername(),
                        userRegistrationDtoB.getUsername()
                );
    }

    @Test
    public void testThatUsersCanBeSuccessfullyRemovedFromTeam() {
        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        underTest.createTeam(teamDtoA);

        UserRegistrationDto userRegistrationDtoA = TestDataCreator.createUserRegistrationDtoA();
        userService.createUser(userRegistrationDtoA);
        UserRegistrationDto userRegistrationDtoB = TestDataCreator.createUserRegistrationDtoB();
        userService.createUser(userRegistrationDtoB);

        Integer userAId = userService.retrieveUserByUsername(userRegistrationDtoA.getUsername()).getId();
        Integer userBId = userService.retrieveUserByUsername(userRegistrationDtoB.getUsername()).getId();
        Integer teamId = underTest.retrieveTeamByName(teamDtoA.getName()).getId();

        underTest.addTeamUsers(teamId, Arrays.asList(userAId, userBId));
        underTest.deleteTeamUser(teamId, userAId);

        List<UserDto> result = underTest.retrieveTeamUsers(teamId);
        assertThat(result).hasSize(1)
                .extracting(UserDto::getUsername)
                .containsExactlyInAnyOrder(
                        userRegistrationDtoB.getUsername()
                );
    }
}
