package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exceptions.AccessDeniedException;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private UserServiceImpl userService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private Item item;
    private ItemDto itemDto;
    private User user1;
    private User user2;
    private Comment comment;

    @BeforeEach
    public void initEach() {
        user1 = new User(1L, "user1", "user1@email.ru");
        user2 = new User(2L, "user2", "user2@email.ru");

        item = new Item(1L,
                "item",
                "item desc",
                true,
                user2,
                null);
        itemDto = new ItemDto(null, "name", "desc", true, null);

        comment = new Comment(1L, "text", item, user1, LocalDateTime.now());
    }


    @Test
    void createItem() {
        when(userService.getUser(Mockito.anyLong())).thenReturn(user2);
        when(itemRepository.save(Mockito.any(Item.class))).thenReturn(item);

        Assertions.assertEquals(itemService.createItem(itemDto, 2L).getId(), 1L);
    }

    @Test
    void patchItem() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            itemService.patchItem(null, ItemMapper.toItemDto(item), 1L);
        });

        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));

        Assertions.assertThrows(AccessDeniedException.class, () -> {
            itemService.patchItem(1L, ItemMapper.toItemDto(item), 1L);
        });
    }

    @Test
    void getItemWithOwnerCheck() {
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        when(commentRepository.findAllByItem_Id(Mockito.anyLong())).thenReturn(new ArrayList<>());
        when(bookingRepository.findAllByItem_IdOrderByStartDesc(Mockito.anyLong())).thenReturn(new ArrayList<>());

        Assertions.assertEquals(itemService.getItemWithOwnerCheck(1L, 2L).getId(), 1L);
    }

    @Test
    void getMyItems() {
        when(userService.getUser(Mockito.anyLong())).thenReturn(user2);
        when(itemRepository.findPageByOwner_Id(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(item));
        when(commentRepository.findAllByItem_Id(Mockito.anyLong())).thenReturn(new ArrayList<>());
        when(bookingRepository.findAllByItem_IdOrderByStartDesc(Mockito.anyLong())).thenReturn(new ArrayList<>());

        Assertions.assertEquals(itemService.getMyItems(2L, 0, 100).get(0).getId(), 1L);
    }

    @Test
    void searchItems() {
        Assertions.assertTrue(itemService.searchItems("", 0, 100).isEmpty());
        when(itemRepository.searchItemsPageContainsTextAvailableTrue(Mockito.anyString(),
                Mockito.anyInt(),
                Mockito.anyInt()))
                .thenReturn(List.of(item));
        Assertions.assertEquals(itemService.searchItems("qqq", 0, 100).get(0).getId(), 1L);
    }

    @Test
    void createComment() {
        when(userService.getUser(Mockito.anyLong())).thenReturn(user1);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findAllByBooker_IdAndStartBeforeAndEndBeforeOrderByStartDesc(Mockito.anyLong(),
                Mockito.any(),
                Mockito.any()))
                .thenReturn(new ArrayList<>());

        Assertions.assertThrows(BadRequestException.class, () -> {
            itemService.createComment(comment, 1L, 1L);
        });
    }
}