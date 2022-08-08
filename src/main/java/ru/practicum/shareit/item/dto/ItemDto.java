package ru.practicum.shareit.item.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private  String description;
    private Boolean available;

    /*
    проверить, если при создание передавать невалидные данные, будет ли срабатывать ошибка при
    создание юзера из юзераДто (типо если имя нул)
     */
}
