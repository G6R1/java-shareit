package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface ItemStorage {

    Item createItem(Item item);

    Item patchItem(Item item);

    Item getItem(Long itemId);

    Collection<Item> getAllItems();

    boolean deleteItem(Long itemId);
}
