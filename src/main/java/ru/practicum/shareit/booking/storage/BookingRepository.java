package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBooker_IdOrderByStartDesc(@NotNull Long bookerId);

    //@Query(value = "select * " +
    //        "from bookings as b " +
    //        "where b.booker_id = ?1 " +
    //        "ORDER BY b.start_time DESC " +
    //        "LIMIT ?3 OFFSET ?2", nativeQuery = true)
    //List<Booking> findPageByBooker_IdOrderByStartDesc(@NotNull Long bookerId,
    //                                                  @NotNull Integer from,
    //                                                  @NotNull Integer size);

    List<Booking> findAllByStatusAndBooker_IdOrderByStartDesc(@NotNull BookingStatus status,
                                                              @NotNull Long bookerId);


    //@Query(value = "select * " +
    //        "from bookings as b " +
    //        "where b.booker_id = ?2 " +
    //        "and b.status like ?1" +
    //        "ORDER BY b.start_time DESC " +
    //        "LIMIT ?4 OFFSET ?3", nativeQuery = true)
    //List<Booking> findPageByStatusAndBooker_IdOrderByStartDesc(@NotNull BookingStatus status,
    //                                                           @NotNull Long bookerId,
    //                                                           @NotNull Integer from,
    //                                                           @NotNull Integer size);


    List<Booking> findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(@NotNull Long bookerId,
                                                                              @NotNull LocalDateTime start,
                                                                              @NotNull LocalDateTime end);

   // @Query(value = "select * " +
   //         "from bookings as b " +
   //         "where b.booker_id = ?1 " +
   //         "and b.start_time  ?1" +
   //         "and b.end_time  ?1" +
   //         "ORDER BY b.start_time DESC " +
   //         "LIMIT ?5 OFFSET ?4", nativeQuery = true)
   // List<Booking> findPageByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(@NotNull Long bookerId,
   //                                                                            @NotNull LocalDateTime start,
   //                                                                            @NotNull LocalDateTime end,
   //                                                                            @NotNull Integer from,
   //                                                                            @NotNull Integer size);


    List<Booking> findAllByBooker_IdAndStartBeforeAndEndBeforeOrderByStartDesc(@NotNull Long bookerId,
                                                                               @NotNull LocalDateTime start,
                                                                               @NotNull LocalDateTime end);

    //List<Booking> findPageByBooker_IdAndStartBeforeAndEndBeforeOrderByStartDesc(@NotNull Long bookerId,
    //                                                                            @NotNull LocalDateTime start,
    //                                                                            @NotNull LocalDateTime end,
    //                                                                            @NotNull Integer from,
    //                                                                            @NotNull Integer size);
//

    List<Booking> findAllByBooker_IdAndStartAfterAndEndAfterOrderByStartDesc(@NotNull Long bookerId,
                                                                             @NotNull LocalDateTime start,
                                                                             @NotNull LocalDateTime end);

   //List<Booking> findPageByBooker_IdAndStartAfterAndEndAfterOrderByStartDesc(@NotNull Long bookerId,
   //                                                                          @NotNull LocalDateTime start,
   //                                                                          @NotNull LocalDateTime end,
   //                                                                          @NotNull Integer from,
   //                                                                          @NotNull Integer size);


    @Query(value = "select b.booking_id, b.start_time, b.end_time, b.item_id, b.booker_id, b.status " +
            "from bookings as b " +
            "left join items as i on i.item_id = b.item_id " +
            "where i.owner_id = ?1 ", nativeQuery = true)
    List<Long> findUserItemsBookingsIds(@NotNull Long ownerId);

    List<Booking> findAllByItem_IdOrderByStartDesc(@NotNull Long itemId);
}
