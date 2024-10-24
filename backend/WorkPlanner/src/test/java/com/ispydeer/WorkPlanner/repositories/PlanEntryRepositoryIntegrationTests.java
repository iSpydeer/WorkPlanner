package com.ispydeer.WorkPlanner.repositories;

import com.ispydeer.WorkPlanner.entities.planEntry.PlanEntry;
import com.ispydeer.WorkPlanner.entities.team.Team;
import com.ispydeer.WorkPlanner.entities.user.User;
import com.ispydeer.WorkPlanner.repositiories.PlanEntryRepository;
import com.ispydeer.WorkPlanner.repositiories.TeamRepository;
import com.ispydeer.WorkPlanner.repositiories.UserRepository;
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
public class PlanEntryRepositoryIntegrationTests {

    @Autowired
    private PlanEntryRepository underTest;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testThatPlanEntryCanBeCreatedAndRecalledById() {
        PlanEntry planEntry = TestDataCreator.createPlanEntryA();
        underTest.save(planEntry);

        Optional<PlanEntry> result = underTest.findById(planEntry.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(planEntry);
    }

    @Test
    public void testThatMultiplePlanEntriesCanBeCreatedAndRecalled() {
        Team team = TestDataCreator.createTeamA();
        teamRepository.save(team);
        User user = TestDataCreator.createUserA();
        userRepository.save(user);

        PlanEntry planEntryA = TestDataCreator.createPlanEntryA();
        planEntryA.setTeam(team);
        planEntryA.setUser(user);
        underTest.save(planEntryA);

        PlanEntry planEntryB = TestDataCreator.createPlanEntryB();
        planEntryB.setTeam(team);
        planEntryB.setUser(user);
        underTest.save(planEntryB);

        List<PlanEntry> result = underTest.findAll().stream().toList();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(planEntryA, planEntryB);
    }

    @Test
    public void testThatPlanEntryCanBeUpdatedAndRecalled() {
        PlanEntry planEntryA = TestDataCreator.createPlanEntryA();
        underTest.save(planEntryA);

        planEntryA.setTitle("Updated Title");
        underTest.save(planEntryA);

        Optional<PlanEntry> result = underTest.findById(planEntryA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(planEntryA);
    }

    @Test
    public void testThatPlanEntryCanBeDeleted() {
        PlanEntry planEntryA = TestDataCreator.createPlanEntryA();
        underTest.save(planEntryA);

        underTest.deleteById(planEntryA.getId());
        Optional<PlanEntry> result = underTest.findById(planEntryA.getId());
        assertThat(result).isNotPresent();
    }
}
