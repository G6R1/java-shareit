package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    final private BookingRepository bookingRepository;
    final private UserService userService;
    final private ItemService itemService;

    public BookingService(BookingRepository bookingRepository, UserService userService, ItemService itemService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.itemService = itemService;
    }


    public Booking createBooking(Booking booking, Long itemId, Long bookerId) {
        if (booking.getId() != null)
            throw new RuntimeException(" Неверное значение id.");

        Item item = itemService.getItem(itemId);
        User booker = userService.getUser(bookerId);

        if (!item.getAvailable())
            throw new ItemNotAvailableException();

        if (Objects.equals(bookerId, item.getOwner().getId()))
            throw new NotFoundException("Владелец не может бронировать собственную вещь.");

        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        return bookingRepository.save(booking);
    }

    public Booking confirmBooking(Long bookingId, Boolean approved, Long requestorId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);

        if (!Objects.equals(booking.getItem().getOwner().getId(), requestorId))
            throw new AccessDeniedException();

        if (booking.getStatus() != BookingStatus.WAITING)
            throw new BadRequestException(" Бронирование не ожидает подтверждения.");

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        return bookingRepository.save(booking);
    }


    public Booking getBooking(Long bookingId, Long requestorId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);

        if (!Objects.equals(booking.getBooker().getId(), requestorId)
                && !Objects.equals(booking.getItem().getOwner().getId(), requestorId))
            throw new NotFoundException(" Только владелец вещи или создатель бронирования могут выполнить запрос.");

        return booking;
    }


    public List<Booking> getAllMyBookings(Long bookerId, BookingState state) {
        //проверка, что пользователь существует
        userService.getUser(bookerId);

        switch (state) {
            case ALL:
                return bookingRepository.findAllByBooker_IdOrderByStartDesc(bookerId);
            case CURRENT:
                return bookingRepository.findAllByStartBeforeAndEndAfterOrderByStartDesc(LocalDateTime.now(),
                        LocalDateTime.now());
            case PAST:
                return bookingRepository.findAllByStartBeforeAndEndBeforeOrderByStartDesc(LocalDateTime.now(),
                        LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findAllByStartAfterAndEndAfterOrderByStartDesc(LocalDateTime.now(),
                        LocalDateTime.now());
            case WAITING:
                return bookingRepository.findAllByStatusOrderByStartDesc(BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findAllByStatusOrderByStartDesc(BookingStatus.REJECTED);
            default:
                throw new RuntimeException("Некорректный параметр state.");
        }
    }


    public List<Booking> getAllBookingsForMyItems(Long ownerId, BookingState state) {
        //проверка, что пользователь существует
        userService.getUser(ownerId);

        List<Long> idsList = bookingRepository.findUserItemsBookingsIds(ownerId);

        if (idsList.isEmpty())
            return new ArrayList<>();

        switch (state) {
            case ALL:
                return idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .collect(Collectors.toList());
            case CURRENT:
                return idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .filter((x) -> x.getStart().isBefore(LocalDateTime.now())
                        && x.getEnd().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case PAST:
                return idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .filter((x) -> x.getStart().isBefore(LocalDateTime.now())
                                && x.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case FUTURE:
                return idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .filter((x) -> x.getStart().isAfter(LocalDateTime.now())
                                && x.getEnd().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case WAITING:
                return idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .filter((x) -> x.getStatus().equals(BookingStatus.WAITING))
                        .collect(Collectors.toList());
            case REJECTED:
                return idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .filter((x) -> x.getStatus().equals(BookingStatus.REJECTED))
                        .collect(Collectors.toList());
            default:
                throw new RuntimeException("Некорректный параметр state.");
        }
    }
}
