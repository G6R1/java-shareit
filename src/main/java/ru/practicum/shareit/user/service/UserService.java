package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DuplicateEmailException;
import ru.practicum.shareit.exceptions.InvalidParamException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User noValidParamUser) {
        if (noValidParamUser.getId() != null)
            throw new RuntimeException(" Неверное значение id.");

        if (noValidParamUser.getName() == null
            || noValidParamUser.getName().isBlank()
            || noValidParamUser.getEmail() == null
            || noValidParamUser.getEmail().isBlank())
            throw new InvalidParamException(" Название и email не могут быть null/empty");

        if (isEmailExists(noValidParamUser.getEmail()))
            throw new DuplicateEmailException();

        return userStorage.createUser(noValidParamUser);
    }

    public User patchUser(Long userId, User noValidParamsUser) {
        if (userId == null)
            throw new RuntimeException(" Неверное значение id.");

        User oldUser = getUser(userId);

        //если email изменился, проверка, не занят ли новый email
        if (!oldUser.getEmail().equals(noValidParamsUser.getEmail())) {
            if (isEmailExists(noValidParamsUser.getEmail()))
                throw new DuplicateEmailException();
        }

        return userStorage.patchUser(new User(userId,
                noValidParamsUser.getName() == null ? oldUser.getName() : noValidParamsUser.getName(),
                noValidParamsUser.getEmail() == null ? oldUser.getEmail() : noValidParamsUser.getEmail()));
    }

    public User getUser(Long userId) {
        if (!isUserIdExists(userId))
            throw new UserNotFoundException();

        return userStorage.getUser(userId);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public boolean deleteUser(Long userId) {
        if (!isUserIdExists(userId))
            throw new UserNotFoundException();

        return userStorage.deleteUser(userId);
    }

    private boolean isEmailExists(String email) {
        return userStorage.getAllUsers().stream().map(User::getEmail).collect(Collectors.toList()).contains(email);
    }

    private boolean isUserIdExists(Long id) {
        return userStorage.getAllUsers().stream().map(User::getId).collect(Collectors.toList()).contains(id);
    }
}
