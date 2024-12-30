package de.unibayreuth.se.taskboard;

import de.unibayreuth.se.taskboard.business.domain.Task;
import de.unibayreuth.se.taskboard.business.domain.User;
import de.unibayreuth.se.taskboard.business.ports.UserService;
import de.unibayreuth.se.taskboard.business.ports.TaskService;

import java.util.List;
import java.util.stream.Collectors;

public class TestFixtures {
    public static final List<User> USERS = List.of(
            new User("Alice"),
            new User("Bob"),
            new User("Charlie")
    );

    public static final List<Task> TASKS = List.of(
            new Task("Task 1", "Description 1"),
            new Task("Task 2", "Description 2"),
            new Task("Task 3", "Description 3")
    );

    public static List<User> createUsers(UserService userService) {
        // TODO: Fix this after resolving the other TODOs.
//        return USERS.stream()
//                .map(userService::create)
//                .collect(Collectors.toList());
        return List.of();
    }

    public static List<Task> createTasks(TaskService taskService) {
        return TASKS.stream()
                .map(taskService::create)
                .collect(Collectors.toList());
    }
}
