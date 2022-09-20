package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item createItem(ItemDto noValidParamsItem, Long creatorId);

    Item patchItem(Long itemId, ItemDto noValidParamsItem, Long requestorId);

    Item getItem(Long itemId);

    /**
     * Метот создан для эндпойнта GET /items/{itemId}, т.к. тесты просят при запросе от владельца давать
     * информацию о прошлом и следующем бронировании, а при иных запросах - нет.
     *
     * @param itemId      -
     * @param requestorId -
     * @return -
     */
    ItemDtoForOwner getItemWithOwnerCheck(Long itemId, Long requestorId);

    List<ItemDtoForOwner> getMyItems(Long ownerId, Integer from, Integer size);

    List<Item> searchItems(String text, Integer from, Integer size);

    /**
     * только тот кто брал в аренду может оставить отзыв и только после окончания аренды
     */
    Comment createComment(Comment comment, Long itemId, Long createrId);
}
