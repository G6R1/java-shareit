package ru.practicum.shareit.exceptions;

public class ItemNotFoundException extends RuntimeException{
    public ItemNotFoundException() {
        super("Вещь не найдена.");
    }
}
