package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        if (booking == null)
            return null;

        return new BookingDto(booking.getId() == null? null : booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                new ItemDto(booking.getItem().getId(),
                        booking.getItem().getName(),
                        booking.getItem().getDescription(),
                        booking.getItem().getAvailable(),
                        booking.getItem().getRequest() == null? null : booking.getItem().getRequest().getId()),
                new UserDto(booking.getBooker().getId(),
                        booking.getBooker().getName(),
                        booking.getBooker().getEmail()),
                booking.getStatus(),
                booking.getItem().getId(),
                booking.getBooker().getId());
    }

    public static Booking toBooking(BookingDto bookingDto, Item item, User booker) {
        if (bookingDto == null)
            return null;

        return new Booking(bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                booker,
                bookingDto.getStatus());
    }

}
