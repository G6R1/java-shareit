package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class UserDto {
    final private String name;
    @Email
    final private String email; //учтите, что два пользователя не могут иметь одинаковый адрес электронной почты
}
