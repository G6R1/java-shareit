package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.practicum.shareit.exceptions.model.ErrorResponse;

import javax.validation.ConstraintViolationException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {
        log.info("error: MethodArgumentNotValidException");
        String defaultMessage = e.getMessage()
                .split("default message")[1]
                .split("]")[0]
                .substring(2);
        return new ErrorResponse(String.format("Ошибка валидации, некорректный параметр %s", defaultMessage));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(final ConstraintViolationException e) {
        log.info("error: ConstraintViolationException");
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleWebClientResponseException(final WebClientResponseException e) {
        log.info("error: WebClientResponseException");
        e.printStackTrace();
        return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString(StandardCharsets.UTF_8));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        log.info("error: MethodArgumentTypeMismatchException");
        if (Objects.requireNonNull(e.getMessage()).contains("ru.practicum.shareit.booking.BookingState"))
            return new ErrorResponse("Unknown state: UNSUPPORTED_STATUS");

        return new ErrorResponse(e.getMessage());
    }
}
