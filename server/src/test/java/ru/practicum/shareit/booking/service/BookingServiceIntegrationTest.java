package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Transactional
@SpringBootTest(
        properties = "db.name=shareit_test",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceIntegrationTest {

    @Autowired
    ItemService itemService;
    @Autowired
    UserService userService;
    @Autowired
    BookingService bookingService;

    User user = new User(null, "user1", "user1@email.ru");
    User booker = new User(null, "booker", "booker@email.ru");
    Item item = new Item(null, "name", "desc", true, null, null);
    ItemDto itemDto = ItemMapper.toItemDto(item);
    BookingDto bookingDto = new BookingDto(null,
            LocalDateTime.now(),
            LocalDateTime.now(),
            null,
            null,
            null,
            1L,
            2L);

    @Test
    void createBooking() {
        userService.createUser(user);
        userService.createUser(booker);
        itemService.createItem(itemDto, 1L);
        var savedBooking = bookingService.createBooking(bookingDto, 2L);
        Assertions.assertEquals(1L, savedBooking.getId());
        Assertions.assertEquals(1L, savedBooking.getItem().getId());
        Assertions.assertEquals(2L, savedBooking.getBooker().getId());
        Assertions.assertEquals(BookingStatus.WAITING, savedBooking.getStatus());
    }

    @Test
    void confirmBooking() {
        userService.createUser(user);
        userService.createUser(booker);
        itemService.createItem(itemDto, 1L);
        bookingService.createBooking(bookingDto, 2L);
        bookingService.confirmBooking(1L, true, 1L);
        var savedBooking = bookingService.getBooking(1L, 1L);
        Assertions.assertEquals(1L, savedBooking.getId());
        Assertions.assertEquals(BookingStatus.APPROVED, savedBooking.getStatus());

    }

    @Test
    void getBooking() {
        userService.createUser(user);
        userService.createUser(booker);
        itemService.createItem(itemDto, 1L);
        bookingService.createBooking(bookingDto, 2L);
        var savedBooking = bookingService.getBooking(1L, 1L);
        Assertions.assertEquals(1L, savedBooking.getId());
        Assertions.assertEquals(1L, savedBooking.getItem().getId());
        Assertions.assertEquals(2L, savedBooking.getBooker().getId());
        Assertions.assertEquals(BookingStatus.WAITING, savedBooking.getStatus());
    }

    @Test
    void getAllMyBookings() throws Exception {
        userService.createUser(user);
        userService.createUser(booker);
        itemService.createItem(itemDto, 1L);
        bookingService.createBooking(bookingDto, 2L);
        TimeUnit.SECONDS.sleep(1);
        Assertions.assertEquals(1,
                bookingService.getAllMyBookings(2L, BookingState.ALL, 0, 100).size());
        Assertions.assertEquals(1,
                bookingService.getAllMyBookings(2L, BookingState.WAITING, 0, 100).size());
        Assertions.assertEquals(0,
                bookingService.getAllMyBookings(2L, BookingState.REJECTED, 0, 100).size());
        Assertions.assertEquals(0,
                bookingService.getAllMyBookings(2L, BookingState.CURRENT, 0, 100).size());
        Assertions.assertEquals(1,
                bookingService.getAllMyBookings(2L, BookingState.PAST, 0, 100).size());
        Assertions.assertEquals(0,
                bookingService.getAllMyBookings(2L, BookingState.FUTURE, 0, 100).size());
    }

    @Test
    void getAllBookingsForMyItems() throws Exception {
        userService.createUser(user);
        userService.createUser(booker);
        itemService.createItem(itemDto, 1L);
        bookingService.createBooking(bookingDto, 2L);
        TimeUnit.SECONDS.sleep(1);
        Assertions.assertEquals(1,
                bookingService.getAllBookingsForMyItems(1L, BookingState.ALL, 0, 100).size());
        Assertions.assertEquals(1,
                bookingService.getAllBookingsForMyItems(1L, BookingState.WAITING, 0, 100).size());
        Assertions.assertEquals(0,
                bookingService.getAllBookingsForMyItems(1L, BookingState.REJECTED, 0, 100).size());
        Assertions.assertEquals(0,
                bookingService.getAllBookingsForMyItems(1L, BookingState.CURRENT, 0, 100).size());
        Assertions.assertEquals(1,
                bookingService.getAllBookingsForMyItems(1L, BookingState.PAST, 0, 100).size());
        Assertions.assertEquals(0,
                bookingService.getAllBookingsForMyItems(1L, BookingState.FUTURE, 0, 100).size());
    }
}