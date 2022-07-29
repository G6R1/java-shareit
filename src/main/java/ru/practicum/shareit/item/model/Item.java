package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data
public class Item {
    private Long id;
    private String name;
    private  String description;
    //статус о том, доступна или нет вещь для аренды
    private boolean available;
    private User owner;
    //если вещь была создана по запросу другого пользователя,
    // то в этом поле будет храниться ссылка на соответствующий запрос.
    private ItemRequest request;


}
