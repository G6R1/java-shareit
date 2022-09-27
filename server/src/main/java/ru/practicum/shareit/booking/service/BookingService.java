package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;


public interface BookingService {
    Booking createBooking(BookingDto booking, Long bookerId);

    Booking confirmBooking(Long bookingId, Boolean approved, Long requestorId);


    Booking getBooking(Long bookingId, Long requestorId);


    List<Booking> getAllMyBookings(Long bookerId, BookingState state, Integer from, Integer size);


    List<Booking> getAllBookingsForMyItems(Long ownerId, BookingState state, Integer from, Integer size);
}
