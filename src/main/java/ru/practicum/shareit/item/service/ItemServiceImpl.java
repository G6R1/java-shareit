package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exceptions.AccessDeniedException;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.InvalidParamException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService{


    final private UserServiceImpl userService;
    final private BookingService bookingService;
    final private BookingRepository bookingRepository;
    final private ItemRepository itemRepository;
    final private CommentRepository commentRepository;

    @Autowired
    public ItemServiceImpl(UserServiceImpl userService, @Lazy BookingService bookingService, BookingRepository bookingRepository, ItemRepository itemRepository, CommentRepository commentRepository) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Item createItem(Item noValidParamsItem) {
        if (noValidParamsItem.getId() != null)
            throw new RuntimeException(" Неверное значение id.");

        //валидируем параметры
        if (noValidParamsItem.getName() == null
                || noValidParamsItem.getName().isBlank()
                || noValidParamsItem.getDescription() == null
                || noValidParamsItem.getDescription().isBlank()
                || noValidParamsItem.getAvailable() == null)
            throw new InvalidParamException(" Название, описание и статус вещи не могут быть null/empty");

        return itemRepository.save(noValidParamsItem);
    }

    @Override
    public Item patchItem(Long itemId, Item noValidParamsItem) {
        if (itemId == null)
            throw new RuntimeException(" Неверное значение id.");

        Item oldItem = getItem(itemId);

        //проверка, что редактирует владелец вещи
        if (!Objects.equals(noValidParamsItem.getOwner().getId(), oldItem.getOwner().getId()))
            throw new AccessDeniedException();


        return itemRepository.save(new Item(itemId,
                noValidParamsItem.getName() == null ? oldItem.getName() : noValidParamsItem.getName(),
                noValidParamsItem.getDescription() == null ? oldItem.getDescription() : noValidParamsItem.getDescription(),
                noValidParamsItem.getAvailable() == null ? oldItem.getAvailable() : noValidParamsItem.getAvailable(),
                oldItem.getOwner(),
                null));
    }

    @Override
    public Item getItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
    }

    /**
     * Метот создан для эндпойнта GET /items/{itemId}, т.к. тесты просят при запросе от владельца давать
     * информацию о прошлом и следующем бронировании, а при иных запросах - нет.
     *
     * @param itemId      -
     * @param requestorId -
     * @return -
     */
    @Override
    public ItemDtoForOwner getItemWithOwnerCheck(Long itemId, Long requestorId) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        List<Comment> comments = commentRepository.findAllByItem_Id(itemId);

        ItemDtoForOwner returnItemDto;
        if (Objects.equals(item.getOwner().getId(), requestorId)) {
            returnItemDto = ItemMapper.ItemDtoForOwnerFromItemAndBookingList(item,
                    bookingRepository.findAllByItem_IdOrderByStartDesc(itemId));
        } else {
            returnItemDto = ItemMapper.toItemDtoForOwner(item, null, null);
        }

        returnItemDto.setComments(comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));

        return returnItemDto;
    }

    @Override
    public List<ItemDtoForOwner> getMyItems(Long ownerId) {
        //проверка, существует ли такой пользователь
        userService.getUser(ownerId);

        List<Item> itemList = itemRepository.findAllByOwner_Id(ownerId);


        List<ItemDtoForOwner> itemDtoForOwners = itemList.stream()
                .map(x -> {
                    List<Booking> bookingList = bookingRepository.findAllByItem_IdOrderByStartDesc(x.getId());
                    return ItemMapper.ItemDtoForOwnerFromItemAndBookingList(x, bookingList);
                })
                .collect(Collectors.toList());

        for (ItemDtoForOwner item : itemDtoForOwners) {
            item.setComments(commentRepository.findAllByItem_Id(item.getId()).stream()
                    .map(CommentMapper::toCommentDto)
                    .collect(Collectors.toList()));
        }

        return itemDtoForOwners;
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    /* не используется
    public boolean deleteItem(Long itemId) {
        return itemStorage.deleteItem(itemId);
    }*/

    @Override
    public Collection<Item> searchItems(String text) {
        if (text.isBlank())
            return new ArrayList<>();

        return itemRepository.searchItemsContainsTextAvailableTrue(text);
    }

    /**
     * только тот кто брал в аренду может оставить отзыв и только после окончания аренды
     */
    @Override
    public Comment createComment(Comment comment, Long itemId, Long createrId) {
        comment.setItem(getItem(itemId)); //здесь произойдет проверка корректности itemId
        comment.setAuthor(userService.getUser(createrId)); //здесь произойдет проверка корректности createrId
        comment.setCreated(LocalDateTime.now());

        if (!bookingService.getAllMyBookings(createrId, BookingState.PAST).stream()
                .map(x -> x.getItem().getId())
                .collect(Collectors.toList())
                .contains(itemId))
            throw new BadRequestException(" Только тот кто брал вещь в аренду может " +
                    "оставить отзыв и только после окончания аренды");

        return commentRepository.save(comment);
    }
}