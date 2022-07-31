package ru.practicum.shareit.user.storage;

import org.apache.catalina.LifecycleState;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface UserStorage {

    User createUser(User user);

    User patchUser(User user);

    User getUser(Long userId);

    Collection<User> getAllUsers();

    boolean deleteUser(Long userId);

}
