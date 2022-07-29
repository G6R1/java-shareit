package ru.practicum.shareit.exeptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exeptions.model.ErrorResponse;

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
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.info("error: " + e.getClass());
        e.printStackTrace();
        return new ErrorResponse("Произошла непредвиденная ошибка " + e.getClass());
    }
}