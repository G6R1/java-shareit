package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    User user1 = new User(1L,
            "qwe",
            "werw@email.ru");
    User user2 = new User(2L,
            "qwe",
            "wer@email.ru");
    Item item = new Item(1L,
            "name",
            "desc",
            true,
            user2,
            null);
    Comment comment = new Comment(1L,
            "text",
            item,
            user1,
            LocalDateTime.now());

    @Test
    void findAllByItem_Id() {
        System.out.println(commentRepository.save(comment));
        Assertions.assertEquals(1, commentRepository.findAllByItem_Id(1L).size());
    }
}