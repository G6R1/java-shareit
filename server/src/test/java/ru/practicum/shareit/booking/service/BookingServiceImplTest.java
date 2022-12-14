package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exceptions.AccessDeniedException;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserService userService;
    @Mock
    private ItemService itemService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Booking booking;
    private Item item;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void initEach() {
        user1 = new User(1L, "user1", "user1@email.ru");
        user2 = new User(2L, "user2", "user2@email.ru");
        user3 = new User(3L, "user3", "user3@email.ru");

        item = new Item(1L,
                "item",
                "item desc",
                true,
                user2,
                null);

        booking = new Booking(1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item,
                user3,
                BookingStatus.APPROVED);

    }

    @Test
    void createBooking() { //?????????????????? ??????????????????
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(itemService.getItem(Mockito.anyLong())).thenReturn(item);
        when(userService.getUser(Mockito.anyLong())).thenReturn(user3);

        bookingService.createBooking(BookingMapper.toBookingDto(booking), 3L);

        Mockito.verify(bookingRepository, Mockito.times(1)).save(booking);
    }

    @Test
    void confirmBooking() {
        booking.setItem(item);

        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(booking));

        Assertions.assertThrows(AccessDeniedException.class, () -> {
            bookingService.confirmBooking(1L, true, 3L);
        });
        Assertions.assertThrows(BadRequestException.class, () -> {
            bookingService.confirmBooking(1L, true, 2L);
        });
    }

    @Test
    void getBooking() {
        booking.setBooker(user1);
        booking.setItem(item);

        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(booking));

        Assertions.assertThrows(NotFoundException.class, () -> {
            bookingService.getBooking(1L, 3L);
        });

    }

    @Test
    void getAllMyBookings() {
        when(userService.getUser(Mockito.anyLong())).thenReturn(user1);
        when(bookingRepository.findAllByBooker_IdAndStartBeforeAndEndBeforeOrderByStartDesc(Mockito.anyLong(),
                Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

        Assertions.assertTrue(bookingService.getAllMyBookings(1L,
                        BookingState.PAST,
                        0,
                        100)
                .isEmpty());
    }

    @Test
    void getAllBookingsForMyItems() {
        when(userService.getUser(Mockito.anyLong())).thenReturn(user1);
        when(bookingRepository.findUserItemsBookingsIds(Mockito.anyLong())).thenReturn(new ArrayList<>());

        Assertions.assertTrue(bookingService.getAllBookingsForMyItems(1L,
                        BookingState.PAST,
                        null,
                        null)
                .isEmpty());
    }
}