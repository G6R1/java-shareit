package ru.practicum.shareit.requests.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.ItemRequestMapper;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserService userService;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private ItemRequest itemRequest;
    private Item item;
    private User user1;
    private User user2;

    @BeforeEach
    public void initEach() {
        itemRequest = new ItemRequest(1L, "itemrequest desc", user1, LocalDateTime.now());

        user1 = new User(1L, "user1", "user1@email.ru");
        user2 = new User(2L, "user2", "user2@email.ru");

        item = new Item(1L,
                "item",
                "item desc",
                true,
                user2,
                null);

    }

    @Test
    void createItemRequest() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            itemRequestService.createItemRequest(ItemRequestMapper.toItemRequestDto(itemRequest), 1L);
        });
    }

    @Test
    void getMyItemRequests() {
        when(userService.getUser(Mockito.anyLong())).thenReturn(user2);
        when(itemRequestRepository.findAllByRequestor_IdOrderByCreatedDesc(Mockito.anyLong()))
                .thenReturn(List.of(itemRequest));
        when(itemRepository.findAllByRequest_Id(Mockito.anyLong())).thenReturn(List.of(item));

        List<ItemRequestDto> list = itemRequestService.getMyItemRequests(2L);
        Assertions.assertEquals(list.size(), 1);
        Assertions.assertEquals(list.get(0).getId(), 1L);
        Assertions.assertEquals(list.get(0).getItems().get(0).getId(), 1L);
    }

    @Test
    void getItemRequests() {
        when(userService.getUser(Mockito.anyLong())).thenReturn(user2);
        when(itemRequestRepository.findAllNotMyRequests(Mockito.anyLong())).thenReturn(new ArrayList<>());

        Assertions.assertTrue(itemRequestService.getItemRequests(null, null, 2L).isEmpty());
    }

    @Test
    void getItemRequest() {
        when(userService.getUser(Mockito.anyLong())).thenReturn(user1);
        when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(itemRequest));
        when(itemRepository.findAllByRequest_Id(Mockito.anyLong())).thenReturn(List.of(item));

        ItemRequestDto itemRequestDto = itemRequestService.getItemRequest(1L, 1L);
        Assertions.assertEquals(itemRequestDto.getId(), 1L);
    }
}