package ru.practicum.shareit.exceptions;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException() {
        super("Этот email уже занят.");
    }
}
