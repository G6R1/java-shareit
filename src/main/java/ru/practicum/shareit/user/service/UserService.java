package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {

    User createUser(User noValidParamUser);

    User patchUser(Long userId, User noValidParamsUser);

    User getUser(Long userId);

    Collection<User> getAllUsers();

    boolean deleteUser(Long userId);

}
