package ru.yandex.practicum.ShareIt.item;

import lombok.Data;
import ru.yandex.practicum.ShareIt.request.ItemRequest;
import ru.yandex.practicum.ShareIt.user.User;

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
