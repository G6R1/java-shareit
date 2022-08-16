package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @NotNull
    @NotBlank
    @Column(name = "name")
    private String name;
    @Email
    @NotNull
    @NotBlank
    @Column(name = "email", unique = true)
    private String email; //учтите, что два пользователя не могут иметь одинаковый адрес электронной почты

}
