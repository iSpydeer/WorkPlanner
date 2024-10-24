package com.ispydeer.WorkPlanner.utilities;

import com.ispydeer.WorkPlanner.entities.planEntry.PlanEntry;
import com.ispydeer.WorkPlanner.entities.planEntry.dto.PlanEntryDto;
import com.ispydeer.WorkPlanner.entities.planEntry.planEntryColor.PlanEntryColor;
import com.ispydeer.WorkPlanner.entities.team.Team;
import com.ispydeer.WorkPlanner.entities.team.dto.TeamDto;
import com.ispydeer.WorkPlanner.entities.user.User;
import com.ispydeer.WorkPlanner.entities.user.dto.UserDto;
import com.ispydeer.WorkPlanner.entities.user.dto.UserRegistrationDto;
import com.ispydeer.WorkPlanner.entities.user.role.Role;

import java.time.LocalDateTime;

/**
 * A utility class that provides static methods to create test data instances
 * for various entity and DTO classes.
 */
public final class TestDataCreator {

    private TestDataCreator() {
    }

    public static User createUserA() {
        User user = new User();
        user.setFirstName("TestNameA");
        user.setLastName("TestLastNameA");
        user.setPassword("passwordA");
        user.setUsername("testUsernameA");
        user.setRole(Role.USER);
        user.setAccountCreationDate(LocalDateTime.now());
        return user;
    }

    public static User createUserB() {
        User user = new User();
        user.setFirstName("TestNameB");
        user.setLastName("TestLastNameB");
        user.setPassword("passwordB");
        user.setUsername("testUsernameB");
        user.setRole(Role.USER);
        user.setAccountCreationDate(LocalDateTime.now());
        return user;
    }

    public static User createUserAdmin() {
        User user = new User();
        user.setFirstName("TestNameAdmin");
        user.setLastName("TestLastNameAdmin");
        user.setPassword("passwordAdmin");
        user.setUsername("testUsernameAdmin");
        user.setRole(Role.ADMIN);
        user.setAccountCreationDate(LocalDateTime.now());
        return user;
    }

    public static UserDto createUserDtoA() {
        UserDto userDto = new UserDto();
        userDto.setFirstName("TestNameDtoA");
        userDto.setLastName("TestLastNameDtoA");
        userDto.setUsername("testUsernameDtoA");
        return userDto;
    }

    public static UserDto createUserDtoB() {
        UserDto userDto = new UserDto();
        userDto.setFirstName("TestNameDtoB");
        userDto.setLastName("TestLastNameDtoB");
        userDto.setUsername("testUsernameDtoB");
        return userDto;
    }

    public static UserRegistrationDto createUserRegistrationDtoA() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setFirstName("TestNameRegDtoA");
        userRegistrationDto.setLastName("TestLastNameRegDtoA");
        userRegistrationDto.setUsername("testUsernameRegDtoA");
        userRegistrationDto.setPassword("passwordRegDtoA");
        return userRegistrationDto;
    }

    public static UserRegistrationDto createUserRegistrationDtoB() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setFirstName("TestNameRegDtoB");
        userRegistrationDto.setLastName("TestLastNameRegDtoB");
        userRegistrationDto.setUsername("testUsernameRegDtoB");
        userRegistrationDto.setPassword("passwordRegDtoB");
        return userRegistrationDto;
    }

    public static Team createTeamA() {
        Team team = new Team();
        team.setName("TeamA");
        team.setDescription("Team A Description");
        team.setTeamCreationDate(LocalDateTime.now());
        return team;
    }

    public static Team createTeamB() {
        Team team = new Team();
        team.setName("TeamB");
        team.setDescription("Team B Description");
        team.setTeamCreationDate(LocalDateTime.now());
        return team;
    }

    public static TeamDto createTeamDtoA() {
        TeamDto teamDto = new TeamDto();
        teamDto.setName("TeamDtoA");
        teamDto.setDescription("Team A Description");
        return teamDto;
    }

    public static TeamDto createTeamDtoB() {
        TeamDto teamDto = new TeamDto();
        teamDto.setName("TeamDtoB");
        teamDto.setDescription("Team B Description");
        return teamDto;
    }

    public static PlanEntry createPlanEntryA() {
        PlanEntry planEntry = new PlanEntry();
        planEntry.setTitle("PlanEntryA");
        planEntry.setStartTime(LocalDateTime.now());
        planEntry.setEndTime(LocalDateTime.now().plusHours(8));
        planEntry.setPlanEntryColor(PlanEntryColor.BLUE);
        return planEntry;
    }

    public static PlanEntry createPlanEntryB() {
        PlanEntry planEntry = new PlanEntry();
        planEntry.setTitle("PlanEntryB");
        planEntry.setStartTime(LocalDateTime.now().minusHours(8));
        planEntry.setEndTime(LocalDateTime.now());
        planEntry.setPlanEntryColor(PlanEntryColor.GREEN);
        return planEntry;
    }

    public static PlanEntryDto createPlanEntryDtoA() {
        PlanEntryDto planEntryDto = new PlanEntryDto();
        planEntryDto.setTitle("PlanEntryDtoA");
        planEntryDto.setStartTime(LocalDateTime.now());
        planEntryDto.setEndTime(LocalDateTime.now().plusHours(8));
        planEntryDto.setPlanEntryColor(PlanEntryColor.BLUE);
        return planEntryDto;
    }

    public static PlanEntryDto createPlanEntryDtoB() {
        PlanEntryDto planEntryDto = new PlanEntryDto();
        planEntryDto.setTitle("PlanEntryDtoB");
        planEntryDto.setStartTime(LocalDateTime.now().minusHours(8));
        planEntryDto.setEndTime(LocalDateTime.now());
        planEntryDto.setPlanEntryColor(PlanEntryColor.GREEN);
        return planEntryDto;
    }
}
