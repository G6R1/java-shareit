package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public User createUser(@Valid @RequestBody User user) {
        User savedUser = userService.createUser(user);
        log.info("Выполнен запрос createUser");
        return savedUser;
    }

    @PatchMapping("/{id}")
    public User patchUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        User oldUser = userService.getUser(id);
        User newUser = new User(id,
                userDto.getName() == null? oldUser.getName() : userDto.getName(),
                userDto.getEmail() == null? oldUser.getEmail() : userDto.getEmail());
        User savedUser = userService.patchUser(newUser);
        log.info("Выполнен запрос patchUser");
        return savedUser;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        User foundUser = userService.getUser(id);
        log.info("Выполнен запрос getUser");
        return foundUser;
    }

    @GetMapping()
    public Collection<User> getAllUser() {
        List<User> userList = List.copyOf(userService.getAllUsers());
        log.info("Выполнен запрос getAllUser");
        return userList;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        log.info("Выполнен запрос deleteUser");
    }
}
