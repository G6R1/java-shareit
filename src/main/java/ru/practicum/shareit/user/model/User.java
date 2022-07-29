package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class User {
    final private Long id;
    @NotNull
    final private String name;
    @Email
    @NotNull
    final private String email; //учтите, что два пользователя не могут иметь одинаковый адрес электронной почты
}
