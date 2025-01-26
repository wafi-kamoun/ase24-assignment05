package de.unibayreuth.se.taskboard.api.mapper;

import de.unibayreuth.se.taskboard.api.dtos.TaskDto;
import de.unibayreuth.se.taskboard.api.dtos.UserDto;
import de.unibayreuth.se.taskboard.business.domain.Task;
import de.unibayreuth.se.taskboard.business.domain.User;
import de.unibayreuth.se.taskboard.business.ports.UserService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Mapper(componentModel = "spring")
@ConditionalOnMissingBean // Prevent IntelliJ warning about duplicate beans
@RequiredArgsConstructor
public abstract class TaskDtoMapper {

    private final UserService userService; // Inject UserService for user fetching
    private final UserDtoMapper userDtoMapper; // Inject UserDtoMapper for DTO mapping

    protected boolean utcNowUpdated = false;
    protected LocalDateTime utcNow;

    /**
     * Maps a Task entity to a TaskDto.
     * Fetches the assignee details using the UserService.
     */
    @Mapping(target = "assignee", expression = "java(getUserById(source.getAssigneeId()))")
    @Mapping(target = "createdAt", expression = "java(mapTimestamp(source.getCreatedAt()))")
    @Mapping(target = "updatedAt", expression = "java(mapTimestamp(source.getUpdatedAt()))")
    public abstract TaskDto fromBusiness(Task source);

    /**
     * Maps a TaskDto to a Task entity.
     * Maps the assigneeId from the UserDto.
     */
    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "status", source = "status", defaultValue = "TODO")
    @Mapping(target = "createdAt", expression = "java(mapTimestamp(source.getCreatedAt()))")
    @Mapping(target = "updatedAt", expression = "java(mapTimestamp(source.getUpdatedAt()))")
    public abstract Task toBusiness(TaskDto source);

    /**
     * Retrieves a UserDto by userId using the UserService.
     * Returns null if userId is null or user cannot be found.
     */
    protected UserDto getUserById(UUID userId) {
        if (userId == null) {
            return null;
        }
        // Assumes userService.getById(userId) returns User, adjust if Optional<User>
        User user = userService.getById(userId);
        if (user == null) {
            return null;
        }
        return userDtoMapper.fromBusiness(user);
    }

    /**
     * Maps a timestamp for createdAt/updatedAt fields.
     * Ensures that null values are replaced with the current UTC timestamp.
     */
    protected LocalDateTime mapTimestamp(LocalDateTime timestamp) {
        if (timestamp == null) {
            // Ensure that createdAt and updatedAt use the same timestamp
            if (!utcNowUpdated) {
                utcNow = LocalDateTime.now(ZoneId.of("UTC"));
                utcNowUpdated = true;
            }
            return utcNow;
        }
        return timestamp;
    }
}
