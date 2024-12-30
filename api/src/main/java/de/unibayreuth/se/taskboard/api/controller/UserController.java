package de.unibayreuth.se.taskboard.api.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@OpenAPIDefinition(
        info = @Info(
                title = "TaskBoard",
                version = "0.0.1"
        )
)
@Tag(name = "Users")
@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    // TODO: Add GET /api/users endpoint to retrieve all users.
    // TODO: Add GET /api/users/{id} endpoint to retrieve a user by ID.
    // TODO: Add POST /api/users endpoint to create a new user based on a provided user DTO.
}
