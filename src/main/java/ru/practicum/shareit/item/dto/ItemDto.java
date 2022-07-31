package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class ItemDto {
    final private Long id;
    final private String name;
    final private  String description;
    final private Boolean available;

    /*
    проверить, если при создание передавать невалидные данные, будет ли срабатывать ошибка при
    создание юзера из юзераДто (типо если имя нул)
     */
}
