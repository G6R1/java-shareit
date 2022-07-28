package ru.yandex.practicum.ShareIt.user;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class User {
    private Long id;
    private String name;
    @Email
    private String email; //учтите, что два пользователя не могут иметь одинаковый адрес электронной почты
}
