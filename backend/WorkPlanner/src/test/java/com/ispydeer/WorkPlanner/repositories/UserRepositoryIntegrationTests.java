package com.ispydeer.WorkPlanner.repositories;

import com.ispydeer.WorkPlanner.entities.user.User;
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
public class UserRepositoryIntegrationTests {

    @Autowired
    private UserRepository underTest;

    @Test
    public void testThatUserCanBeCreatedAndRecalledById() {
        User user = TestDataCreator.createUserA();
        underTest.save(user);

        Optional<User> result = underTest.findById(user.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    public void testThatUserCanBeCreatedAndRecalledByUsername() {
        User user = TestDataCreator.createUserA();
        underTest.save(user);

        Optional<User> result = underTest.findByUsername(user.getUsername());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    public void testThatUserCanBeCreatedAndCheckedByUsername() {
        User user = TestDataCreator.createUserA();
        underTest.save(user);

        boolean result = underTest.existsByUsername(user.getUsername());
        assertThat(result).isTrue();
    }

    @Test
    public void testThatMultipleUsersCanBeCreatedAndRecalled() {
        User userA = TestDataCreator.createUserA();
        User userB = TestDataCreator.createUserB();
        underTest.save(userA);
        underTest.save(userB);

        List<User> result = underTest.findAll().stream().toList();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(userA, userB);
    }

    @Test
    public void testThatUserCanBeUpdatedAndRecalled() {
        User userA = TestDataCreator.createUserA();
        underTest.save(userA);

        userA.setFirstName("UpdatedFirstName");
        underTest.save(userA);

        Optional<User> result = underTest.findById(userA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userA);
    }

    @Test
    public void testThatUserCanBeDeleted() {
        User userA = TestDataCreator.createUserA();
        underTest.save(userA);

        underTest.deleteById(userA.getId());
        Optional<User> result = underTest.findById(userA.getId());
        assertThat(result).isNotPresent();
    }
}
