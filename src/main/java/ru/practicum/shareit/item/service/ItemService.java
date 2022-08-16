package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exceptions.AccessDeniedException;
import ru.practicum.shareit.exceptions.InvalidParamException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ItemService {


    final private UserService userService;
    final private BookingRepository bookingRepository;
    final private ItemRepository itemRepository;

    @Autowired
    public ItemService(UserService userService, BookingRepository bookingRepository, ItemRepository itemRepository) {
        this.userService = userService;
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
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

        return itemRepository.save(noValidParamsItem);
    }

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

    public Item getItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
    }

    /**
     * Метот создан для эндпойнта GET /items/{itemId}, т.к. тесты просят при запросе от владельца давать
     * информацию о прошлом и следующем бронировании, а при иных запросах - нет.
     * @param itemId -
     * @param requestorId -
     * @return -
     */
    public ItemDtoForOwner getItemWithOwnerCheck(Long itemId, Long requestorId) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        if (Objects.equals(item.getOwner().getId(), requestorId))
            return ItemMapper.ItemDtoForOwnerFromItemAndBookingList(item,
                    bookingRepository.findAllByItem_IdOrderByStartDesc(itemId));

        return ItemMapper.toItemDtoForOwner(item, null, null);
    }

    public List<ItemDtoForOwner> getMyItems(Long ownerId) {
        //проверка, существует ли такой пользователь
        userService.getUser(ownerId);

        List<Item> itemList = itemRepository.findAllByOwner_Id(ownerId);

        return itemList.stream().map(x -> {
            List<Booking> bookingList = bookingRepository.findAllByItem_IdOrderByStartDesc(x.getId());
            return ItemMapper.ItemDtoForOwnerFromItemAndBookingList(x, bookingList);
        }).collect(Collectors.toList());
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    /* не используется
    public boolean deleteItem(Long itemId) {
        return itemStorage.deleteItem(itemId);
    }*/

    public Collection<Item> searchItems(String text) {
        if (text.isBlank())
            return new ArrayList<>();

        return itemRepository.findAllByNameContainingIgnoreCaseAndAvailableTrueOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text);
    }
}
