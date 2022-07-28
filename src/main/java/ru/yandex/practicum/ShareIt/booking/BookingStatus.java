package ru.yandex.practicum.ShareIt.booking;

/**
 *     WAITING — новое бронирование, ожидает одобрения, APPROVED —
 *     бронирование подтверждено владельцем, REJECTED — бронирование
 *     отклонено владельцем, CANCELED — бронирование отменено создателем.
 */
public enum BookingStatus {
    WAITING, APPROVED, REJECTED, CANCELED;
}
