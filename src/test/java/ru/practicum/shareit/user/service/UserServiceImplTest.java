package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.AccessDeniedException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.service.ItemRequestServiceImpl;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void initEach() {
        user = new User(1L, "user1", "user1@email.ru");
    }

    @Test
    void createUser() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            userService.createUser(user);
        });
        user.setId(null);

        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        userRepository.save(user);

        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(User.class));
    }

    @Test
    void patchUser() {
        user.setId(null);
        Assertions.assertThrows(RuntimeException.class, () -> {
            userService.patchUser(1L, user);
        });

    }

    @Test
    void getUser() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        userService.getUser(1L);
        Mockito.verify(userRepository, Mockito.times(1))
                .findById(Mockito.anyLong());
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        Assertions.assertTrue(userService.getAllUsers().isEmpty());
    }
}