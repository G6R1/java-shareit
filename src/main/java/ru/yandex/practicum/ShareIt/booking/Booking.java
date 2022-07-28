package ru.yandex.practicum.ShareIt.booking;

import lombok.Data;
import ru.yandex.practicum.ShareIt.item.Item;
import ru.yandex.practicum.ShareIt.user.User;

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
