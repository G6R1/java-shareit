package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@SpringBootTest(
        properties = "db.name=shareit_test",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = ShareItServer.class)
class UserServiceIntegrationTest {

    @Autowired
    UserService userService;

    User user = new User(null, "user1", "user1@email.ru");
    User userPatch = new User(1L, "patch", "user1@email.ru");

    @Test
    void createUser() {
        User savedUser = userService.createUser(user);
        Assertions.assertEquals(1L, savedUser.getId());
        Assertions.assertEquals("user1", savedUser.getName());
        Assertions.assertEquals("user1@email.ru", savedUser.getEmail());
    }

    @Test
    void patchUser() {
        userService.createUser(user);
        User savedUser = userService.patchUser(1L, userPatch);
        Assertions.assertEquals(1L, savedUser.getId());
        Assertions.assertEquals("patch", savedUser.getName());
    }

    @Test
    void getUser() {
        userService.createUser(user);
        User savedUser = userService.getUser(1L);
        Assertions.assertEquals(1L, savedUser.getId());
        Assertions.assertEquals("user1", savedUser.getName());
        Assertions.assertEquals("user1@email.ru", savedUser.getEmail());
    }

    @Test
    void getAllUsers() {
        userService.createUser(user);
        List<User> savedUsers = userService.getAllUsers();
        Assertions.assertEquals(1, savedUsers.size());
        Assertions.assertEquals(1L, savedUsers.get(0).getId());
        Assertions.assertEquals("user1", savedUsers.get(0).getName());
        Assertions.assertEquals("user1@email.ru", savedUsers.get(0).getEmail());
    }

    @Test
    void deleteUser() {
        userService.createUser(user);
        userService.deleteUser(1L);
        List<User> savedUsers = userService.getAllUsers();
        Assertions.assertEquals(0, savedUsers.size());
    }
}