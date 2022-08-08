package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    private Long id;
    @NotNull
    @NotBlank
    @Column(name = "name")
    private String name;
    @Email
    @NotNull
    @NotBlank
    @Column(name = "email")
    private String email; //учтите, что два пользователя не могут иметь одинаковый адрес электронной почты
}
