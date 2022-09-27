package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Добавление нового запроса на бронирование.
     * Запрос может быть создан любым пользователем, а затем подтверждён владельцем вещи.
     * Эндпоинт — POST /bookings.
     * После создания запрос находится в статусе WAITING — «ожидает подтверждения».
     */
    @PostMapping()
    public BookingDto createBooking(@RequestBody BookingDto bookingDto,
                                    @RequestHeader("X-Sharer-User-Id") Long id) {


        Booking booking = bookingService.createBooking(bookingDto, id);
        log.info("Выполнен запрос createBooking");
        return BookingMapper.toBookingDto(booking);
    }

    /**
     * Подтверждение или отклонение запроса на бронирование.
     * Может быть выполнено только владельцем вещи.
     * Затем статус бронирования становится либо APPROVED, либо REJECTED.
     * Эндпоинт — PATCH /bookings/{bookingId}?approved={approved},
     *
     * @param bookingId -
     * @param approved  может принимать значения true или false.
     * @return -
     */
    @PatchMapping("{bookingId}")
    public BookingDto confirmBooking(@PathVariable Long bookingId,
                                     @RequestParam Boolean approved,
                                     @RequestHeader("X-Sharer-User-Id") Long id) {


        Booking booking = bookingService.confirmBooking(bookingId, approved, id);
        log.info("Выполнен запрос confirmBooking");
        return BookingMapper.toBookingDto(booking);
    }

    /**
     * Получение данных о конкретном бронировании (включая его статус).
     * Может быть выполнено либо автором бронирования, либо владельцем вещи, к которой относится бронирование.
     * Эндпоинт — GET /bookings/{bookingId}.
     *
     * @param bookingId -
     * @param id        -
     * @return -
     */
    @GetMapping("{bookingId}")
    public BookingDto getBooking(@PathVariable Long bookingId,
                                 @RequestHeader("X-Sharer-User-Id") Long id) {
        Booking booking = bookingService.getBooking(bookingId, id);
        log.info("Выполнен запрос getBooking");
        return BookingMapper.toBookingDto(booking);
    }

    /**
     * Получение списка всех бронирований текущего пользователя.
     * Эндпоинт — GET /bookings?state={state}. Параметр state необязательный и по умолчанию равен ALL (англ. «все»).
     * Также он может принимать значения CURRENT (англ. «текущие»), **PAST** (англ. «завершённые»),
     * FUTURE (англ. «будущие»), WAITING (англ. «ожидающие подтверждения»), REJECTED (англ. «отклонённые»).
     * Бронирования должны возвращаться отсортированными по дате от более новых к более старым.
     *
     * @param state -
     * @param id    -
     * @return -
     */
    @GetMapping
    public List<BookingDto> getAllMyBookings(@RequestParam(required = false, defaultValue = "ALL") BookingState state,
                                             @RequestHeader("X-Sharer-User-Id") Long id,
                                             @RequestParam(required = false, defaultValue = "0") Integer from,
                                             @RequestParam(required = false, defaultValue = "100") Integer size) {

        List<Booking> bookingList = bookingService.getAllMyBookings(id, state, from, size);
        log.info("Выполнен запрос getAllMyBookings");
        return bookingList.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    /**
     * Получение списка бронирований для всех вещей текущего пользователя.
     * Эндпоинт — GET /bookings/owner?state={state}.
     * Этот запрос имеет смысл для владельца хотя бы одной вещи.
     * Работа параметра state аналогична getAllMyBookings.
     *
     * @param state -
     * @param id    -
     * @return -
     */
    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForMyItems(@RequestParam(required = false, defaultValue = "ALL") BookingState state,
                                                     @RequestHeader("X-Sharer-User-Id") Long id,
                                                     @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                     @RequestParam(required = false, defaultValue = "100") @Positive Integer size) {

        List<Booking> bookingList = bookingService.getAllBookingsForMyItems(id, state, from, size);
        log.info("Выполнен запрос getAllBookingsForMyItems");
        return bookingList.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }
}

