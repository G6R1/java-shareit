package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;

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
    public User create(@Valid @RequestBody User user) {

        log.info("");
        return ;
    }

    @PatchMapping()
    public User create(@Valid @RequestBody User user) {

        log.info("");
        return ;
    }

    @GetMapping()
    public User create(@Valid @RequestBody User user) {

        log.info("");
        return ;
    }

    @DeleteMapping()
    public User create(@Valid @RequestBody User user) {

        log.info("");
        return ;
    }
}
