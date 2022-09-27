package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User createUser(User noValidParamUser);

    User patchUser(Long userId, User noValidParamsUser);

    User getUser(Long userId);

    List<User> getAllUsers();

    boolean deleteUser(Long userId);

}
