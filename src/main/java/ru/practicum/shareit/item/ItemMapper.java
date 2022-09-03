package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForItemRequest;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        if (item == null)
            return null;

        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() == null? null : item.getRequest().getId());
    }

    public static Item toItem(ItemDto itemDto, User owner, ItemRequest request) {
        if (itemDto == null)
            return null;

        return new Item(itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                owner,
                request);
    }

    public static ItemDtoForOwner toItemDtoForOwner(Item item, Booking last, Booking next) {
        if (item == null)
            return null;

        return new ItemDtoForOwner(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() == null? null : item.getRequest().getId(),
                BookingMapper.toBookingDto(last),
                BookingMapper.toBookingDto(next),
                null);
    }

    public static ItemDtoForOwner ItemDtoForOwnerFromItemAndBookingList(Item item, List<Booking> bookingList) {

        if (bookingList.isEmpty())
            return ItemMapper.toItemDtoForOwner(item, null, null);

        if (bookingList.size() == 1)
            return bookingList.get(0).getStart().isAfter(LocalDateTime.now()) ?
                    ItemMapper.toItemDtoForOwner(item, null, bookingList.get(0)) :
                    ItemMapper.toItemDtoForOwner(item, bookingList.get(0), null);

        Booking last = null;
        Booking next = null;

        for (Booking booking : bookingList) {
            if (booking.getStart().isBefore(LocalDateTime.now())) {
                last = booking;
                int index = bookingList.indexOf(booking);

                if (index != 0) {
                    next = bookingList.get(index - 1);
                }

                break;
            }
        }

        if (last == null)
            next = bookingList.get(bookingList.size() - 1);

        return ItemMapper.toItemDtoForOwner(item, last, next);
    }

    public static ItemDtoForItemRequest toItemDtoForItemRequest(Item item) {
        if (item == null)
            return null;

        return new ItemDtoForItemRequest(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() == null? null : item.getRequest().getId());
    }
}
