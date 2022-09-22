package ru.practicum.shareit.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("Пользователь не найден.");
    }
}