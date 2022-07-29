package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {

    public UserDto toUserDto(User user) {
        return null;

        /*
        return new ItemDto(
        item.getName(),
        item.getDescription(),
        item.isAvailable(),
        item.getRequest() != null ? item.getRequest().getId() : null
        );
         */
    }
}
