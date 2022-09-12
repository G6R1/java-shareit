package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Transactional
@SpringBootTest(
        properties = "db.name=shareit_test",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceIntegrationTest {

    @Autowired
    ItemService itemService;
    @Autowired
    UserService userService;
    @Autowired
    BookingService bookingService;

    User user = new User(null, "user1", "user1@email.ru");
    User commentator = new User(null, "commentator", "commentator@email.ru");
    Item item = new Item(null, "name", "desc", true, null, null);
    ItemDto itemDto = ItemMapper.toItemDto(item);
    Comment comment = new Comment(null, "text", null, null, LocalDateTime.now());
    Booking booking = new Booking(null,
            LocalDateTime.now(),
            LocalDateTime.now(),
            null,
            null,
            null);

    @Test
    void createItem() {
        userService.createUser(user);
        Item savedItem = itemService.createItem(itemDto, 1L);
        Assertions.assertEquals(1L, savedItem.getId());
        Assertions.assertEquals("name", savedItem.getName());
        Assertions.assertEquals("desc", savedItem.getDescription());
    }

    @Test
    void patchItem() {
        userService.createUser(user);
        itemService.createItem(itemDto, 1L);
        itemDto.setDescription("patch");
        Item savedItem = itemService.patchItem(1L, itemDto, 1L);
        Assertions.assertEquals(1L, savedItem.getId());
        Assertions.assertEquals("name", savedItem.getName());
        Assertions.assertEquals("patch", savedItem.getDescription());
    }

    @Test
    void getItem() {
        userService.createUser(user);
        itemService.createItem(itemDto, 1L);
        Item savedItem = itemService.getItem(1L);
        Assertions.assertEquals(1L, savedItem.getId());
        Assertions.assertEquals("name", savedItem.getName());
        Assertions.assertEquals("desc", savedItem.getDescription());
    }

    @Test
    void getItemWithOwnerCheck() {
        userService.createUser(user);
        itemService.createItem(itemDto, 1L);
        Item savedItem = itemService.getItem(1L);
        Assertions.assertEquals(1L, savedItem.getId());
        Assertions.assertEquals("name", savedItem.getName());
        Assertions.assertEquals("desc", savedItem.getDescription());
    }

    @Test
    void getMyItems() {
        userService.createUser(user);
        itemService.createItem(itemDto, 1L);
        List<ItemDtoForOwner> savedItems = itemService.getMyItems(1L, 0, 100);
        Assertions.assertEquals(1, savedItems.size());
        Assertions.assertEquals(1L, savedItems.get(0).getId());
        Assertions.assertEquals("name", savedItems.get(0).getName());
        Assertions.assertEquals("desc", savedItems.get(0).getDescription());
    }

    @Test
    void searchItems() {
        userService.createUser(user);
        itemService.createItem(itemDto, 1L);
        List<Item> savedItems = itemService.searchItems("name", 0, 100);
        Assertions.assertEquals(1, savedItems.size());
        Assertions.assertEquals(1L, savedItems.get(0).getId());
        Assertions.assertEquals("name", savedItems.get(0).getName());
        Assertions.assertEquals("desc", savedItems.get(0).getDescription());
    }

    @Test
    void createComment() throws Exception {
        userService.createUser(user);
        userService.createUser(commentator);
        itemService.createItem(itemDto, 1L);
        bookingService.createBooking(booking, 1L, 2L);
        TimeUnit.SECONDS.sleep(1);
        Comment savedComment = itemService.createComment(comment, 1L, 2L);
        Assertions.assertEquals(1L, savedComment.getId());
        Assertions.assertEquals("text", savedComment.getText());
        Assertions.assertEquals(1L, savedComment.getItem().getId());
        Assertions.assertEquals(2L, savedComment.getAuthor().getId());
    }
}