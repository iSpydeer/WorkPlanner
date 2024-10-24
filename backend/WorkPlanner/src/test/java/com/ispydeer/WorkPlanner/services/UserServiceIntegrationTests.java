package com.ispydeer.WorkPlanner.services;

import com.ispydeer.WorkPlanner.controllers.exceptions.UserNotFoundException;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class UserServiceIntegrationTests {

    @Autowired
    private UserService underTest;
    @Autowired
    private TeamService teamService;

    @Test
    public void testThatUserIsSuccessfullySavedAndRecalledByUsername() {
        UserRegistrationDto userRegistrationDtoA = TestDataCreator.createUserRegistrationDtoA();
        underTest.createUser(userRegistrationDtoA);

        UserDto result = underTest.retrieveUserByUsername(userRegistrationDtoA.getUsername());
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(userRegistrationDtoA.getUsername());
    }

    @Test
    public void testThatUserIsNotFoundByUsername() {
        assertThatThrownBy(() -> underTest.retrieveUserByUsername("NonExistentUser"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void testThatUserIsSuccessfullySavedAndRecalledById() {
        UserRegistrationDto userRegistrationDtoA = TestDataCreator.createUserRegistrationDtoA();
        underTest.createUser(userRegistrationDtoA);

        Integer id = underTest.retrieveUserByUsername(userRegistrationDtoA.getUsername()).getId();
        UserDto result = underTest.retrieveUserById(id);
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(userRegistrationDtoA.getUsername());
    }

    @Test
    public void testThatUserIsNotFoundById() {
        final int ID_OF_NON_EXISTING_USER = 9999;
        assertThatThrownBy(() -> underTest.retrieveUserById(ID_OF_NON_EXISTING_USER))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void testThatUsersAreSuccessfullySavedAndRecalled() {
        UserRegistrationDto userRegistrationDtoA = TestDataCreator.createUserRegistrationDtoA();
        UserRegistrationDto userRegistrationDtoB = TestDataCreator.createUserRegistrationDtoB();
        underTest.createUser(userRegistrationDtoA);
        underTest.createUser(userRegistrationDtoB);

        List<UserDto> result = underTest.retrieveAllUsers();
        assertThat(result)
                .hasSize(2)
                .extracting(UserDto::getUsername)
                .containsExactlyInAnyOrder(
                        userRegistrationDtoA.getUsername(),
                        userRegistrationDtoB.getUsername()
                );
    }

    @Test
    public void testThatUserIsSuccessfullyDeleted() {
        UserRegistrationDto userRegistrationDtoA = TestDataCreator.createUserRegistrationDtoA();
        underTest.createUser(userRegistrationDtoA);

        Integer id = underTest.retrieveUserByUsername(userRegistrationDtoA.getUsername()).getId();
        underTest.deleteUser(id);
        assertThatThrownBy(
                () -> underTest.retrieveUserById(id)
        ).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void testThatTeamsCanBeSuccessfullyAssignedToUser() {
        UserRegistrationDto userRegistrationDtoA = TestDataCreator.createUserRegistrationDtoA();
        underTest.createUser(userRegistrationDtoA);

        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        TeamDto teamDtoB = TestDataCreator.createTeamDtoB();
        teamService.createTeam(teamDtoA);
        teamService.createTeam(teamDtoB);

        Integer id = underTest.retrieveUserByUsername(userRegistrationDtoA.getUsername()).getId();
        Integer teamAId = teamService.retrieveTeamByName(teamDtoA.getName()).getId();
        Integer teamBId = teamService.retrieveTeamByName(teamDtoB.getName()).getId();

        underTest.addUserTeam(id, teamAId);
        underTest.addUserTeam(id, teamBId);

        List<TeamDto> result = underTest.retrieveUserTeams(id);
        assertThat(result).hasSize(2)
                .extracting(TeamDto::getName)
                .containsExactlyInAnyOrder(
                        teamDtoA.getName(),
                        teamDtoB.getName()
                );
    }

    @Test
    public void testThatTeamsCanBeSuccessfullyUnassignedFromUser() {
        UserRegistrationDto userRegistrationDtoA = TestDataCreator.createUserRegistrationDtoA();
        underTest.createUser(userRegistrationDtoA);

        TeamDto teamDtoA = TestDataCreator.createTeamDtoA();
        TeamDto teamDtoB = TestDataCreator.createTeamDtoB();
        teamService.createTeam(teamDtoA);
        teamService.createTeam(teamDtoB);

        Integer id = underTest.retrieveUserByUsername(userRegistrationDtoA.getUsername()).getId();
        Integer teamAId = teamService.retrieveTeamByName(teamDtoA.getName()).getId();
        Integer teamBId = teamService.retrieveTeamByName(teamDtoB.getName()).getId();

        underTest.addUserTeam(id, teamAId);
        underTest.addUserTeam(id, teamBId);
        underTest.deleteUserTeam(id, teamAId);

        List<TeamDto> result = underTest.retrieveUserTeams(id);
        assertThat(result).hasSize(1)
                .extracting(TeamDto::getName)
                .containsExactlyInAnyOrder(
                        teamDtoB.getName()
                );
    }
}












