package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    User user1 = new User(1L, "user1", "user1@email.ru");
    User user2 = new User(2L, "user2", "user2@email.ru");
    ItemRequest itemRequest = new ItemRequest(1L, "desc", user2, LocalDateTime.now());
    Item item1 = new Item(1L,
            "name1",
            "desc1",
            true,
            user1,
            itemRequest);
    Item item2 = new Item(2L,
            "name2",
            "desc2",
            true,
            user2,
            null);

    @BeforeEach
    public void beforeEach() {
        itemRepository.save(item1);
        itemRepository.save(item2);
    }

    @Test
    void findAllByOwner_Id() {
        Assertions.assertEquals(1, itemRepository.findAllByOwner_Id(1L).size());
    }

    @Test
    void findPageByOwner_Id() {
        Assertions.assertEquals(1, itemRepository.findPageByOwner_Id(1L, 0, 5).size());
    }

    @Test
    void searchItemsContainsTextAvailableTrue() {
        Assertions.assertEquals(2, itemRepository.searchItemsContainsTextAvailableTrue("name").size());
        Assertions.assertEquals(1, itemRepository.searchItemsContainsTextAvailableTrue("name1").size());
    }

    @Test
    void searchItemsPageContainsTextAvailableTrue() {
        Assertions.assertEquals(2,
                itemRepository.searchItemsPageContainsTextAvailableTrue("name", 0, 5).size());
        Assertions.assertEquals(1,
                itemRepository.searchItemsPageContainsTextAvailableTrue("name1", 0, 5).size());
    }

    @Test
    void findAllByRequest_Id() {
        Assertions.assertEquals(1, itemRepository.findAllByRequest_Id(1L).size());
    }
}