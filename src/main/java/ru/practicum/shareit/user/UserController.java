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
import java.util.stream.Collectors;

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
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        User savedUser = userService.createUser(UserMapper.toUser(userDto));
        log.info("Выполнен запрос createUser");
        return UserMapper.toUserDto(savedUser);
    }

    @PatchMapping("/{id}")
    public UserDto patchUser(@PathVariable Long id, @Valid @RequestBody UserDto userdto) {
        User savedUser = userService.patchUser(id, UserMapper.toUser(userdto));
        log.info("Выполнен запрос patchUser");
        return UserMapper.toUserDto(savedUser);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        User foundUser = userService.getUser(id);
        log.info("Выполнен запрос getUser");
        return UserMapper.toUserDto(foundUser);
    }

    @GetMapping()
    public Collection<UserDto> getAllUser() {
        List<User> userList = List.copyOf(userService.getAllUsers());
        log.info("Выполнен запрос getAllUser");
        return userList.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        log.info("Выполнен запрос deleteUser");
    }
}
