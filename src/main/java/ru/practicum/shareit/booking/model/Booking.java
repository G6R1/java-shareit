package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

@Data
public class Booking {
    private Long id;
    private Instant start; //дата и время начала бронирования
    private Instant end; //дата и время конца бронирования
    private Item item; //вещь, которую пользователь бронирует
    private User booker; //пользователь, который осуществляет бронирование
    private BookingStatus status;
}
