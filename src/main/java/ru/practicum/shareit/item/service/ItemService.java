package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
public class ItemService {

    ItemStorage itemStorage;
    UserService userService;

    @Autowired
    public ItemService(ItemStorage itemStorage, UserService userService) {
        this.itemStorage = itemStorage;
        this.userService = userService;
    }

    public Item createItem(Item item) {
        if (item.getId() != null)
            throw new RuntimeException(" Неверное значение id.");

        //проверка, существует ли юзер с таким id
        userService.getUser(item.getOwnerId());

        return itemStorage.createItem(item);
    }

    public Item patchItem(Item item) {
        //проверка, что редактирует владелец вещи



    }

    public Item getItem(Long itemId) {

    }

    public Collection<Item> getAllItems() {

    }

    public boolean deleteItem(Long itemId) {

    }
}
