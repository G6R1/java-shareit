package ru.practicum.shareit.exceptions;

public class BookingNotFoundException extends RuntimeException{
    public BookingNotFoundException() {
        super("Бронирование не найдено.");
    }
}
