package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes= ShareItServer.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @MockBean
    UserService userService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    User user = new User(null, "user", "user@email.ru");
    UserDto userDto = new UserDto(null, "user", "user@email.ru");
    User userWithId = new User(1L, "user", "user@email.ru");
    User userPatch = new User(1L, "patch", "patch@email.ru");

    @Test
    void createUser() throws Exception {
        when(userService.createUser(Mockito.any(User.class))).thenReturn(userWithId);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("user")))
                .andExpect(jsonPath("$.email", is("user@email.ru")));
    }

    @Test
    void patchUser() throws Exception {
        when(userService.patchUser(Mockito.anyLong(), Mockito.any(User.class))).thenReturn(userPatch);

        mockMvc.perform(patch("/users/{id}", 1L)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("patch")))
                .andExpect(jsonPath("$.email", is("patch@email.ru")));
    }

    @Test
    void getUser() throws Exception {
        when(userService.getUser(Mockito.anyLong())).thenReturn(userWithId);

        mockMvc.perform(get("/users/{id}", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("user")))
                .andExpect(jsonPath("$.email", is("user@email.ru")));
    }

    @Test
    void getAllUser() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(userWithId));

        mockMvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].name", is("user")))
                .andExpect(jsonPath("$.[0].email", is("user@email.ru")));
    }

    @Test
    void deleteUser() throws Exception {
        when(userService.deleteUser(Mockito.anyLong())).thenReturn(true);

        mockMvc.perform(delete("/users/{id}", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}