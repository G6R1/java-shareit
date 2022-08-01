package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class FakeItemStorage implements ItemStorage{

    final private Map<Long, Item> inMemoryStorage;
    private Long idCounter;

    public FakeItemStorage() {
        this.inMemoryStorage = new HashMap<>();
        idCounter = 1L;
    }

    @Override
    public Item createItem(Item item) {
        Item itemForSave = new Item(idCounter, item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwnerId(),
                item.getRequest());
        inMemoryStorage.put(idCounter++, itemForSave);
        return itemForSave;
    }

    @Override
    public Item patchItem(Item item) {
        inMemoryStorage.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getItem(Long itemId) {
        return inMemoryStorage.get(itemId);
    }

    @Override
    public Collection<Item> getMyItems(Long ownerId) {
        return inMemoryStorage.values().stream().
                filter((x) -> Objects.equals(x.getOwnerId(), ownerId)).
                collect(Collectors.toList());
    }

    @Override
    public Collection<Item> getAllItems() {
        return inMemoryStorage.values();
    }

    @Override
    public boolean deleteItem(Long itemId) {
        inMemoryStorage.remove(itemId);
        return true;
    }
}
