package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public User createUser(User user) {
        if (user.getId() != null)
            throw new RuntimeException(" Неверное значение id.");

        if (isEmailExists(user.getEmail()))
            throw new RuntimeException(" Этот email уже занят.");

        return userStorage.createUser(user);
    }

    public User patchUser(User user) {
        if (user.getId() == null)
            throw new RuntimeException(" Неверное значение id.");

        if (!isIdExists(user.getId()))
            throw new RuntimeException(" Неверное значение id.");

        //если email изменился, проверка, не занят ли новый email
        if (!user.getEmail().equals(getUser(user.getId()).getEmail())) {
            if (isEmailExists(user.getEmail()))
                throw new RuntimeException(" Этот email уже занят.");
        }

        return userStorage.patchUser(user);
    }

    public User getUser(Long userId) {
        if (!isIdExists(userId))
            throw new RuntimeException(" Неверное значение id.");

        return userStorage.getUser(userId);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public boolean deleteUser(Long userId) {
        if (!isIdExists(userId))
            throw new RuntimeException(" Неверное значение id.");

        return userStorage.deleteUser(userId);
    }

    private boolean isEmailExists(String email) {
        return userStorage.getAllUsers().stream().map(User::getEmail).collect(Collectors.toList()).contains(email);
    }

    private boolean isIdExists(Long id) {
        return userStorage.getAllUsers().stream().map(User::getId).collect(Collectors.toList()).contains(id);
    }
}
