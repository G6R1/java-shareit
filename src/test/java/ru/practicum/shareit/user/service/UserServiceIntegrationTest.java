package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "db.name=shareit_test",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceIntegrationTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    User user = new User(null, "user1", "user1@email.ru");
    User userPatch = new User(1L, "patch", "user1@email.ru");

    @Test
    void createUser() {
        User savedUsed = userService.createUser(user);
        Assertions.assertEquals(1L, savedUsed.getId());
        Assertions.assertEquals("user1", savedUsed.getName());
        Assertions.assertEquals("user1@email.ru", savedUsed.getEmail());
    }

    @Test
    void patchUser() {
        userService.createUser(user);
        User savedUsed = userService.patchUser(1L, userPatch);
        Assertions.assertEquals(1L, savedUsed.getId());
        Assertions.assertEquals("patch", savedUsed.getName());
    }

    @Test
    void getUser() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void deleteUser() {
    }
}