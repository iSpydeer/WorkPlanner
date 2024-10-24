package com.ispydeer.WorkPlanner.entities.user;

import com.ispydeer.WorkPlanner.entities.planEntry.PlanEntry;
import com.ispydeer.WorkPlanner.entities.team.Team;
import com.ispydeer.WorkPlanner.entities.user.role.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a user.
 * Each user can belong to multiple teams and have associated plan entries.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity(name = "BasicUser")
public class User {

    /**
     * The id of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * The username of the user.
     */
    private String username;

    /**
     * The first name of the user.
     */
    private String firstName;

    /**
     * The last name of the user.
     */
    private String lastName;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * The role of the user within the application.
     * This defines the permissions and access level of the user.
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * The date when the user was created.
     */
    private LocalDateTime accountCreationDate;

    /**
     * A set of teams that this user is a member of.
     */
    @ManyToMany(mappedBy = "setOfUsers")
    private Set<Team> setOfTeams = new HashSet<>();

    /**
     * A set of plan entries associated with this user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PlanEntry> planEntries = new HashSet<>();

    /**
     * Constructs a new User with the specified username, first name, and last name.
     *
     * @param username  the username of the user
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     */
    public User(String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
