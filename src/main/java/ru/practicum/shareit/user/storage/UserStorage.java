package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {

    User createUser(User user);

    User patchUser(User user);

    User getUser(Long userId);

    Collection<User> getAllUsers();

    boolean deleteUser(Long userId);

}
