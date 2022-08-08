package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.AccessDeniedException;
import ru.practicum.shareit.exceptions.InvalidParamException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ItemService {

    ItemStorage itemStorage;
    UserService userService;

    @Autowired
    public ItemService(ItemStorage itemStorage, UserService userService) {
        this.itemStorage = itemStorage;
        this.userService = userService;
    }

    public Item createItem(Item noValidParamsItem) {
        if (noValidParamsItem.getId() != null)
            throw new RuntimeException(" Неверное значение id.");

        //валидируем параметры т.к. не можем валидировать аннотациями из-за приема DTO
        if (noValidParamsItem.getName() == null
                || noValidParamsItem.getName().isBlank()
                || noValidParamsItem.getDescription() == null
                || noValidParamsItem.getDescription().isBlank()
                || noValidParamsItem.getAvailable() == null)
            throw new InvalidParamException(" Название, описание и статус вещи не могут быть null/empty");

        return itemStorage.createItem(noValidParamsItem);
    }

    public Item patchItem(Long itemId, Item noValidParamsItem) {
        if (itemId == null)
            throw new RuntimeException(" Неверное значение id.");

        Item oldItem = getItem(itemId);

        //проверка, что редактирует владелец вещи
        if (!Objects.equals(noValidParamsItem.getOwner().getId(), oldItem.getOwner().getId()))
            throw new AccessDeniedException();

        return itemStorage.patchItem(new Item(itemId,
                noValidParamsItem.getName() == null ? oldItem.getName() : noValidParamsItem.getName(),
                noValidParamsItem.getDescription() == null ? oldItem.getDescription() : noValidParamsItem.getDescription(),
                noValidParamsItem.getAvailable() == null ? oldItem.getAvailable() : noValidParamsItem.getAvailable(),
                oldItem.getOwner(),
                null));
    }

    public Item getItem(Long itemId) {
        if (!isItemIdExists(itemId))
            throw new RuntimeException(" Несуществующий id.");

        return itemStorage.getItem(itemId);
    }

    public Collection<Item> getMyItems(Long ownerId) {
        //проверка, существует ли такой пользователь
        userService.getUser(ownerId);

        return itemStorage.getMyItems(ownerId);
    }

    public Collection<Item> getAllItems() {
        return itemStorage.getAllItems();
    }

    /* не используется
    public boolean deleteItem(Long itemId) {
        return itemStorage.deleteItem(itemId);
    }*/

    public Collection<Item> searchItems(String text) {
        if (text.isBlank())
            return new ArrayList<>();

        List<Item> items = new ArrayList<>(getAllItems());
        return items.stream()
                .filter((x) -> x.getName().toLowerCase().contains(text.toLowerCase())
                        || x.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }

    private boolean isItemIdExists(Long id) {
        return itemStorage.getAllItems().stream().map(Item::getId).collect(Collectors.toList()).contains(id);
    }
}
