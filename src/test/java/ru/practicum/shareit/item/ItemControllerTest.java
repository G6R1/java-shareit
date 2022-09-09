package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.requests.ItemRequestController;
import ru.practicum.shareit.requests.service.ItemRequestService;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {


    @MockBean
    ItemService itemService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    

    @Test
    void createItem() {
    }

    @Test
    void patchItem() {
    }

    @Test
    void getItem() {
    }

    @Test
    void getMyItems() {
    }

    @Test
    void searchItems() {
    }

    @Test
    void createComment() {
    }
}