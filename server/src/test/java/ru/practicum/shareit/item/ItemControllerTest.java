package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@ContextConfiguration(classes = ShareItServer.class)
class ItemControllerTest {


    @MockBean
    ItemService itemService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    User user = new User(1L, "user", "user@email.ru");
    User commentator = new User(2L, "commentator", "commentator@email.ru");
    Item item = new Item(null, "item", "item desc", true, user, null);
    Item itemWithId = new Item(1L, "item", "item desc", true, user, null);
    ItemDto itemDto = ItemMapper.toItemDto(item);
    ItemDtoForOwner itemDtoForOwner = new ItemDtoForOwner(1L,
            "item",
            "item desc",
            true,
            null,
            null,
            null,
            null);
    Comment comment = new Comment(null, "text", item, commentator, LocalDateTime.now());
    Comment commentWithId = new Comment(1L, "text", item, commentator, LocalDateTime.now());

    @Test
    void createItem() throws Exception {
        when(itemService.createItem(Mockito.any(ItemDto.class), Mockito.anyLong()))
                .thenReturn(itemWithId);

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("item")))
                .andExpect(jsonPath("$.description", is("item desc")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    void patchItem() throws Exception {
        when(itemService.patchItem(Mockito.anyLong(), Mockito.any(ItemDto.class), Mockito.anyLong()))
                .thenReturn(itemWithId);

        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("item")))
                .andExpect(jsonPath("$.description", is("item desc")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    void getItem() throws Exception {
        when(itemService.getItemWithOwnerCheck(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemDtoForOwner);

        mockMvc.perform(get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("item")))
                .andExpect(jsonPath("$.description", is("item desc")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    void getMyItems() throws Exception {
        when(itemService.getMyItems(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(List.of(itemDtoForOwner));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].name", is("item")))
                .andExpect(jsonPath("$.[0].description", is("item desc")))
                .andExpect(jsonPath("$.[0].available", is(true)));
    }

    @Test
    void searchItems() throws Exception {
        when(itemService.searchItems(Mockito.anyString(), Mockito.any(), Mockito.any()))
                .thenReturn(List.of(itemWithId));

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "textForSearch")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].name", is("item")))
                .andExpect(jsonPath("$.[0].description", is("item desc")))
                .andExpect(jsonPath("$.[0].available", is(true)));
    }

    @Test
    void createComment() throws Exception {
        when(itemService.createComment(Mockito.any(Comment.class), Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(commentWithId);

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .content(mapper.writeValueAsString(comment))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is("text")))
                .andExpect(jsonPath("$.authorName", is("commentator")));
    }
}