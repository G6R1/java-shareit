package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.requests.model.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Item {
    final private Long id;
    @NotNull
    @NotBlank
    final private String name;
    @NotNull
    @NotBlank
    final private String description;
    @NotNull
    final private Boolean available; //статус о том, доступна или нет вещь для аренды
    final private Long ownerId;
    //если вещь была создана по запросу другого пользователя,
    // то в этом поле будет храниться ссылка на соответствующий запрос.
    final private ItemRequest request;


}
