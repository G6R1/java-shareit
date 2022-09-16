package ru.practicum.shareit.requests;

import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        if (itemRequest == null)
            return null;

        return new ItemRequestDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                null);
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User requestor) {
        if (itemRequestDto == null)
            return null;

        return new ItemRequest(itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                requestor,
                itemRequestDto.getCreated());
    }
}
