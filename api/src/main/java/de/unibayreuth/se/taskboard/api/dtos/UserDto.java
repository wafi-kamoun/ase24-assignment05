package de.unibayreuth.se.taskboard.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

// Data Transfer Object for User
public record UserDto(
        UUID id,
        @NotBlank(message = "Name must not be blank.")
        String name,
        @NotNull(message = "Role must not be null.")
        String role
) {

         }
