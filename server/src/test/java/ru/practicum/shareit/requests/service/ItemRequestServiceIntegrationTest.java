package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.requests.ItemRequestMapper;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@SpringBootTest(
        properties = "db.name=shareit_test",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = ShareItServer.class)
class ItemRequestServiceIntegrationTest {

    @Autowired
    ItemRequestService itemRequestService;
    @Autowired
    UserService userService;

    User requestor = new User(null, "requestor", "requestor@email.ru");
    User user = new User(null, "user", "user@email.ru");
    ItemRequest itemRequest = new ItemRequest(null, "desc", null, LocalDateTime.now());
    ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);

    @Test
    void createItemRequest() {
        userService.createUser(requestor);
        ItemRequestDto savedItemRequestDto = itemRequestService.createItemRequest(itemRequestDto, 1L);
        Assertions.assertEquals(1L, savedItemRequestDto.getId());
        Assertions.assertEquals("desc", savedItemRequestDto.getDescription());
    }

    @Test
    void getMyItemRequests() {
        userService.createUser(requestor);
        itemRequestService.createItemRequest(itemRequestDto, 1L);
        List<ItemRequestDto> savedItemsRequestDto = itemRequestService.getMyItemRequests(1L);
        Assertions.assertEquals(1, savedItemsRequestDto.size());
        Assertions.assertEquals(1L, savedItemsRequestDto.get(0).getId());
        Assertions.assertEquals("desc", savedItemsRequestDto.get(0).getDescription());
    }

    @Test
    void getItemRequests() {
        userService.createUser(requestor);
        userService.createUser(user);
        itemRequestService.createItemRequest(itemRequestDto, 1L);
        List<ItemRequestDto> savedItemsRequestDto = itemRequestService.getItemRequests(0,
                100,
                2L);
        Assertions.assertEquals(1, savedItemsRequestDto.size());
        Assertions.assertEquals(1L, savedItemsRequestDto.get(0).getId());
        Assertions.assertEquals("desc", savedItemsRequestDto.get(0).getDescription());
    }

    @Test
    void getItemRequest() {
        userService.createUser(requestor);
        itemRequestService.createItemRequest(itemRequestDto, 1L);
        ItemRequestDto savedItemRequestDto = itemRequestService.getItemRequest(1L, 1L);
        Assertions.assertEquals(1L, savedItemRequestDto.getId());
        Assertions.assertEquals("desc", savedItemRequestDto.getDescription());
    }
}