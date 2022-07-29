package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    User getUser(Long userId);

    User getAllUsers();

    boolean deleteUser(Long userId);

}
