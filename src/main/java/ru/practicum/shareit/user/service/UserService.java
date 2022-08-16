package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DuplicateEmailException;
import ru.practicum.shareit.exceptions.InvalidParamException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;

@Service
public class UserService {

    final private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User noValidParamUser) {
        if (noValidParamUser.getId() != null)
            throw new RuntimeException(" Неверное значение id.");

        if (noValidParamUser.getName() == null
            || noValidParamUser.getName().isBlank()
            || noValidParamUser.getEmail() == null
            || noValidParamUser.getEmail().isBlank())
            throw new InvalidParamException(" Название и email не могут быть null/empty");

        return userRepository.save(noValidParamUser);
    }

    public User patchUser(Long userId, User noValidParamsUser) {
        if (userId == null)
            throw new RuntimeException(" Неверное значение id.");

        User oldUser = getUser(userId);

        return userRepository.save(new User(userId,
                noValidParamsUser.getName() == null ? oldUser.getName() : noValidParamsUser.getName(),
                noValidParamsUser.getEmail() == null ? oldUser.getEmail() : noValidParamsUser.getEmail()));
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean deleteUser(Long userId) {
        if (!isUserIdExists(userId))
            throw new UserNotFoundException();

        userRepository.deleteById(userId);
        return true;
    }

    private boolean isUserIdExists(Long id) {
        return userRepository.findById(id).isPresent();
    }
}
