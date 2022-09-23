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
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    public BookingServiceImpl(BookingRepository bookingRepository, UserService userService, ItemService itemService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.itemService = itemService;
    }


    @Override
    public Booking createBooking(Booking booking, Long itemId, Long bookerId) {
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
    public List<Booking> getAllMyBookings(Long bookerId, BookingState state, Integer from, Integer size) {
        //проверка, что пользователь существует
        userService.getUser(bookerId);

        List<Booking> bookingList;

        switch (state) {
            case ALL:
                bookingList = bookingRepository.findAllByBooker_IdOrderByStartDesc(bookerId);

                return bookingList.stream().skip(from).limit(size).collect(Collectors.toList());
            case CURRENT:
                bookingList = bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId,
                        LocalDateTime.now(),
                        LocalDateTime.now());

                return bookingList.stream().skip(from).limit(size).collect(Collectors.toList());
            case PAST:
                bookingList = bookingRepository.findAllByBooker_IdAndStartBeforeAndEndBeforeOrderByStartDesc(bookerId,
                        LocalDateTime.now(),
                        LocalDateTime.now());

                return bookingList.stream().skip(from).limit(size).collect(Collectors.toList());
            case FUTURE:
                bookingList = bookingRepository.findAllByBooker_IdAndStartAfterAndEndAfterOrderByStartDesc(bookerId,
                        LocalDateTime.now(),
                        LocalDateTime.now());

                return bookingList.stream().skip(from).limit(size).collect(Collectors.toList());
            case WAITING:
                bookingList = bookingRepository.findAllByStatusAndBooker_IdOrderByStartDesc(BookingStatus.WAITING,
                        bookerId);

                return bookingList.stream().skip(from).limit(size).collect(Collectors.toList());
            case REJECTED:
                bookingList = bookingRepository.findAllByStatusAndBooker_IdOrderByStartDesc(BookingStatus.REJECTED,
                        bookerId);

                return bookingList.stream().skip(from).limit(size).collect(Collectors.toList());
            default:
                throw new RuntimeException("Некорректный параметр state.");
        }
    }


    @Override
    public List<Booking> getAllBookingsForMyItems(Long ownerId, BookingState state, Integer from, Integer size) {
        //проверка, что пользователь существует
        userService.getUser(ownerId);

        List<Long> idsList = bookingRepository.findUserItemsBookingsIds(ownerId);

        if (idsList.isEmpty())
            return new ArrayList<>();

        List<Booking> bookingList;

        switch (state) {
            case ALL:
                bookingList = idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .sorted((x, y) -> y.getStart().compareTo(x.getStart()))
                        .collect(Collectors.toList());

                return bookingList.stream().skip(from).limit(size).collect(Collectors.toList());
            case CURRENT:
                bookingList = idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .filter((x) -> x.getStart().isBefore(LocalDateTime.now())
                                && x.getEnd().isAfter(LocalDateTime.now()))
                        .sorted((x, y) -> y.getStart().compareTo(x.getStart()))
                        .collect(Collectors.toList());

                return bookingList.stream().skip(from).limit(size).collect(Collectors.toList());
            case PAST:
                bookingList = idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .filter((x) -> x.getStart().isBefore(LocalDateTime.now())
                                && x.getEnd().isBefore(LocalDateTime.now()))
                        .sorted((x, y) -> y.getStart().compareTo(x.getStart()))
                        .collect(Collectors.toList());

                return bookingList.stream().skip(from).limit(size).collect(Collectors.toList());
            case FUTURE:
                bookingList = idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .filter((x) -> x.getStart().isAfter(LocalDateTime.now())
                                && x.getEnd().isAfter(LocalDateTime.now()))
                        .sorted((x, y) -> y.getStart().compareTo(x.getStart()))
                        .collect(Collectors.toList());

                return bookingList.stream().skip(from).limit(size).collect(Collectors.toList());
            case WAITING:
                bookingList = idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .filter((x) -> x.getStatus().equals(BookingStatus.WAITING))
                        .sorted((x, y) -> y.getStart().compareTo(x.getStart()))
                        .collect(Collectors.toList());

                return bookingList.stream().skip(from).limit(size).collect(Collectors.toList());
            case REJECTED:
                bookingList = idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .filter((x) -> x.getStatus().equals(BookingStatus.REJECTED))
                        .sorted((x, y) -> y.getStart().compareTo(x.getStart()))
                        .collect(Collectors.toList());

                return bookingList.stream().skip(from).limit(size).collect(Collectors.toList());
            default:
                throw new RuntimeException("Некорректный параметр state.");
        }
    }
}
