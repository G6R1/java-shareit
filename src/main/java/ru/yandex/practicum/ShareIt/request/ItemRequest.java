package ru.yandex.practicum.ShareIt.request;

import lombok.Data;
import ru.yandex.practicum.ShareIt.user.User;

import java.time.Instant;

@Data
public class ItemRequest {
    private Long id;
    private String description;
    private User requestor;
    private Instant created; //дата и время создания запроса

}
