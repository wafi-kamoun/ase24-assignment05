package de.unibayreuth.se.taskboard.business.ports;

import java.util.Collection;
import java.util.UUID;

import de.unibayreuth.se.taskboard.business.domain.User;

public interface UserService {

    User update(User user);

    void delete(UUID id);

    User create(User user);

    User getById(UUID id);

    Collection<User> getAll();
}
