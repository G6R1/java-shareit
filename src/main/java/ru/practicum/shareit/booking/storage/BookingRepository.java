package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    public List<Booking> findAllByBooker_IdOrderByStartDesc(@NotNull Long bookerId);

    public List<Booking> findAllByStatusOrderByStartDesc(@NotNull BookingStatus status);


    public List<Booking> findAllByStartBeforeAndEndAfterOrderByStartDesc(@NotNull LocalDateTime start,
                                                                         @NotNull LocalDateTime end);

    public List<Booking> findAllByStartBeforeAndEndBeforeOrderByStartDesc(@NotNull LocalDateTime start,
                                                                          @NotNull LocalDateTime end);

    public List<Booking> findAllByStartAfterAndEndAfterOrderByStartDesc(@NotNull LocalDateTime start,
                                                                        @NotNull LocalDateTime end);

    @Query(value = "select b.booking_id, b.start_time, b.end_time, b.item_id, b.booker_id, b.status "+
            "from bookings as b " +
            "left join items as i on i.item_id = b.item_id "+
            "where i.owner_id = ?1 ", nativeQuery = true)
    public List<Long> findUserItemsBookingsIds(@NotNull Long ownerId);

    public List<Booking> findAllByItem_IdOrderByStartDesc(@NotNull Long itemId);
}
