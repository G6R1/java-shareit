package ru.practicum.shareit.exceptions;

public class ItemNotAvailableException extends RuntimeException{
    public ItemNotAvailableException() {
        super("Вещь недоступна для бронирования.");
    }
}
