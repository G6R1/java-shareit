package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {


    private final UserServiceImpl userService;
    private final ItemRequestRepository itemRequestRepository;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ItemServiceImpl(UserServiceImpl userService,
                           ItemRequestRepository itemRequestRepository,
                           BookingRepository bookingRepository,
                           ItemRepository itemRepository,
                           CommentRepository commentRepository) {
        this.userService = userService;
        this.itemRequestRepository = itemRequestRepository;
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Item createItem(ItemDto itemDto, Long creatorId) {

        Item noValidParamsItem = ItemMapper.toItem(itemDto,
                userService.getUser(creatorId),
                itemDto.getRequestId() == null ? null : itemRequestRepository.findById(itemDto.getRequestId())
                        .orElseThrow(NotFoundException::new));

        return itemRepository.save(noValidParamsItem);
    }

    @Override
    public Item patchItem(Long itemId, ItemDto itemDto, Long requestorId) {

        Item oldItem = getItem(itemId);

        //проверка, что редактирует владелец вещи
        if (!Objects.equals(requestorId, oldItem.getOwner().getId()))
            throw new AccessDeniedException();

        return itemRepository.save(new Item(itemId,
                itemDto.getName() == null ? oldItem.getName() : itemDto.getName(),
                itemDto.getDescription() == null ? oldItem.getDescription() : itemDto.getDescription(),
                itemDto.getAvailable() == null ? oldItem.getAvailable() : itemDto.getAvailable(),
                oldItem.getOwner(),
                itemDto.getRequestId() == null ? null : itemRequestRepository.findById(itemDto.getRequestId())
                        .orElseThrow(NotFoundException::new)));
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
            returnItemDto = ItemMapper.itemDtoForOwnerFromItemAndBookingList(item,
                    bookingRepository.findAllByItem_IdOrderByStartDesc(itemId));
        } else {
            returnItemDto = ItemMapper.toItemDtoForOwner(item, null, null);
        }

        returnItemDto.setComments(comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));

        return returnItemDto;
    }

    @Override
    public List<ItemDtoForOwner> getMyItems(Long ownerId, Integer from, Integer size) {
        //проверка, существует ли такой пользователь
        userService.getUser(ownerId);

        List<Item> itemList = itemRepository.findPageByOwner_Id(ownerId, from, size);

        List<ItemDtoForOwner> itemDtoForOwners = itemList.stream()
                .map(x -> {
                    List<Booking> bookingList = bookingRepository.findAllByItem_IdOrderByStartDesc(x.getId());
                    return ItemMapper.itemDtoForOwnerFromItemAndBookingList(x, bookingList);
                })
                .collect(Collectors.toList());

        for (ItemDtoForOwner item : itemDtoForOwners) {
            item.setComments(commentRepository.findAllByItem_Id(item.getId()).stream()
                    .map(CommentMapper::toCommentDto)
                    .collect(Collectors.toList()));
        }

        return itemDtoForOwners;
    }

    /* не используется
    private List<Item> getAllItems() {
        return itemRepository.findAll();
    }*/

    /* не используется
    public boolean deleteItem(Long itemId) {
        return itemStorage.deleteItem(itemId);
    }*/

    @Override
    public List<Item> searchItems(String text, Integer from, Integer size) {
        if (text.isBlank())
            return new ArrayList<>();

        return itemRepository.searchItemsPageContainsTextAvailableTrue(text, from, size);
    }

    /**
     * только тот кто брал в аренду может оставить отзыв и только после окончания аренды
     */
    @Override
    public Comment createComment(Comment comment, Long itemId, Long createrId) {
        comment.setItem(getItem(itemId)); //здесь произойдет проверка корректности itemId
        comment.setAuthor(userService.getUser(createrId)); //здесь произойдет проверка корректности createrId
        comment.setCreated(LocalDateTime.now());

        if (!bookingRepository.findAllByBooker_IdAndStartBeforeAndEndBeforeOrderByStartDesc(createrId,
                        LocalDateTime.now(),
                        LocalDateTime.now()).stream()
                .map(x -> x.getItem().getId())
                .collect(Collectors.toList())
                .contains(itemId))
            throw new BadRequestException(" Только тот кто брал вещь в аренду может " +
                    "оставить отзыв и только после окончания аренды");

        return commentRepository.save(comment);
    }
}
