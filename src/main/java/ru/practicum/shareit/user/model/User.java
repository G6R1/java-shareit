package ru.practicum.shareit.user.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class User {
    final private Long id;
    @NotNull
    @NotBlank
    final private String name;
    @Email
    @NotNull
    @NotBlank
    final private String email; //учтите, что два пользователя не могут иметь одинаковый адрес электронной почты
}
