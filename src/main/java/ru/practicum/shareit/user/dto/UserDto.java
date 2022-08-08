package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    @Email
    private String email; //учтите, что два пользователя не могут иметь одинаковый адрес электронной почты
}
