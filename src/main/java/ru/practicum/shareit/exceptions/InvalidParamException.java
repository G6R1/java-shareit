package ru.practicum.shareit.exceptions;

public class InvalidParamException extends RuntimeException{
    public InvalidParamException(String message) {
        super(message);
    }
}
