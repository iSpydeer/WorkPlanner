package com.ispydeer.WorkPlanner.repositories;

import com.ispydeer.WorkPlanner.entities.team.Team;
import com.ispydeer.WorkPlanner.repositiories.TeamRepository;
import com.ispydeer.WorkPlanner.utilities.TestDataCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith({SpringExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class TeamRepositoryIntegrationTests {

    @Autowired
    private TeamRepository underTest;

    @Test
    public void testThatTeamCanBeCreatedAndRecalledById() {
        Team teamA = TestDataCreator.createTeamA();
        underTest.save(teamA);

        Optional<Team> result = underTest.findById(teamA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(teamA);
    }

    @Test
    public void testThatTeamCanBeCreatedAndRecalledByName() {
        Team teamA = TestDataCreator.createTeamA();
        underTest.save(teamA);

        Optional<Team> result = underTest.findByName(teamA.getName());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(teamA);
    }

    @Test
    public void testThatTeamCanBeCreatedAndCheckedByName() {
        Team teamA = TestDataCreator.createTeamA();
        underTest.save(teamA);

        boolean result = underTest.existsByName(teamA.getName());
        assertThat(result).isTrue();
    }

    @Test
    public void testThatMultipleTeamsCanBeCreatedAndRecalled() {
        Team teamA = TestDataCreator.createTeamA();
        Team teamB = TestDataCreator.createTeamB();
        underTest.save(teamA);
        underTest.save(teamB);

        List<Team> result = underTest.findAll().stream().toList();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(teamA, teamB);
    }

    @Test
    public void testThatTeamCanBeUpdatedAndRecalled() {
        Team teamA = TestDataCreator.createTeamA();
        underTest.save(teamA);

        teamA.setName("UpdatedTeamA");
        underTest.save(teamA);

        Optional<Team> result = underTest.findById(teamA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(teamA);
    }

    @Test
    public void testThatTeamCanBeDeleted() {
        Team teamA = TestDataCreator.createTeamA();
        underTest.save(teamA);

        underTest.deleteById(teamA.getId());
        Optional<Team> result = underTest.findById(teamA.getId());
        assertThat(result).isNotPresent();
    }
}
