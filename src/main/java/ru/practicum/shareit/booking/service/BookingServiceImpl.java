package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    final private BookingRepository bookingRepository;
    final private UserServiceImpl userService;
    final private ItemServiceImpl itemService;

    public BookingServiceImpl(BookingRepository bookingRepository, UserServiceImpl userService, ItemServiceImpl itemService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.itemService = itemService;
    }


    @Override
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

    @Override
    public Booking confirmBooking(Long bookingId, Boolean approved, Long requestorId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);

        if (!Objects.equals(booking.getItem().getOwner().getId(), requestorId))
            throw new AccessDeniedException();

        if (booking.getStatus() != BookingStatus.WAITING)
            throw new BadRequestException(" Бронирование не ожидает подтверждения.");

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        return bookingRepository.save(booking);
    }


    @Override
    public Booking getBooking(Long bookingId, Long requestorId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);

        if (!Objects.equals(booking.getBooker().getId(), requestorId)
                && !Objects.equals(booking.getItem().getOwner().getId(), requestorId))
            throw new NotFoundException(" Только владелец вещи или создатель бронирования могут выполнить запрос.");

        return booking;
    }


    @Override
    public List<Booking> getAllMyBookings(Long bookerId, BookingState state) {
        //проверка, что пользователь существует
        userService.getUser(bookerId);

        switch (state) {
            case ALL:
                return bookingRepository.findAllByBooker_IdOrderByStartDesc(bookerId);
            case CURRENT:
                return bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId,
                        LocalDateTime.now(),
                        LocalDateTime.now());
            case PAST:
                return bookingRepository.findAllByBooker_IdAndStartBeforeAndEndBeforeOrderByStartDesc(bookerId,
                        LocalDateTime.now(),
                        LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findAllByBooker_IdAndStartAfterAndEndAfterOrderByStartDesc(bookerId,
                        LocalDateTime.now(),
                        LocalDateTime.now());
            case WAITING:
                return bookingRepository.findAllByStatusAndBooker_IdOrderByStartDesc(BookingStatus.WAITING,
                        bookerId);
            case REJECTED:
                return bookingRepository.findAllByStatusAndBooker_IdOrderByStartDesc(BookingStatus.REJECTED,
                        bookerId);
            default:
                throw new RuntimeException("Некорректный параметр state.");
        }
    }


    @Override
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
                        .sorted((x, y) -> y.getStart().compareTo(x.getStart()))
                        .collect(Collectors.toList());
            case CURRENT:
                return idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .filter((x) -> x.getStart().isBefore(LocalDateTime.now())
                                && x.getEnd().isAfter(LocalDateTime.now()))
                        .sorted((x, y) -> y.getStart().compareTo(x.getStart()))
                        .collect(Collectors.toList());
            case PAST:
                return idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .filter((x) -> x.getStart().isBefore(LocalDateTime.now())
                                && x.getEnd().isBefore(LocalDateTime.now()))
                        .sorted((x, y) -> y.getStart().compareTo(x.getStart()))
                        .collect(Collectors.toList());
            case FUTURE:
                return idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .filter((x) -> x.getStart().isAfter(LocalDateTime.now())
                                && x.getEnd().isAfter(LocalDateTime.now()))
                        .sorted((x, y) -> y.getStart().compareTo(x.getStart()))
                        .collect(Collectors.toList());
            case WAITING:
                return idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .filter((x) -> x.getStatus().equals(BookingStatus.WAITING))
                        .sorted((x, y) -> y.getStart().compareTo(x.getStart()))
                        .collect(Collectors.toList());
            case REJECTED:
                return idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .filter((x) -> x.getStatus().equals(BookingStatus.REJECTED))
                        .sorted((x, y) -> y.getStart().compareTo(x.getStart()))
                        .collect(Collectors.toList());
            default:
                throw new RuntimeException("Некорректный параметр state.");
        }
    }
}
