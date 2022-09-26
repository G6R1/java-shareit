package ru.practicum.shareit.user.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.model.User;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@ContextConfiguration(classes = ShareItServer.class)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    User user = new User(1L, "user1", "user1@email.ru");

    @Test
    void findByEmail() {
        userRepository.save(user);

        Assertions.assertEquals(userRepository.findByEmail("user1@email.ru").get().getEmail(), user.getEmail());
    }
}