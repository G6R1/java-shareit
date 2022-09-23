package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.InvalidParamException;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient userClient;

    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @PostMapping()
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        if (userDto.getId() != null)
            throw new InvalidParamException(" Неверное значение id.");

        if (userDto.getName() == null
                || userDto.getName().isBlank()
                || userDto.getEmail() == null
                || userDto.getEmail().isBlank())
            throw new InvalidParamException(" Название и email не могут быть null/empty");

        UserDto savedUser = userClient.createUser(userDto);
        log.info("Выполнен запрос createUser");
        return savedUser;
    }

    @PatchMapping("/{id}")
    public UserDto patchUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        if (id == null)
            throw new InvalidParamException(" Неверное значение id.");

        UserDto savedUser = userClient.patchUser(id, userDto);
        log.info("Выполнен запрос patchUser");
        return savedUser;
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        UserDto user = userClient.getUser(id);
        log.info("Выполнен запрос getUser");
        return user;
    }

    @GetMapping()
    public List<UserDto> getAllUser() {
        List<UserDto> userList = userClient.getAllUser();
        log.info("Выполнен запрос getAllUser");
        return userList;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userClient.deleteUser(id);
        log.info("Выполнен запрос deleteUser");
    }

}
