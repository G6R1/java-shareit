package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {

    ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping()
    public ItemDto createItem(@Valid @RequestBody Item item,
                              @RequestHeader("X-Sharer-User-Id") Long id) {
        Item itemWithOwnerId = new Item(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                id,
                null);
        ItemDto savedItemDto = ItemMapper.toItemDto(itemService.createItem(itemWithOwnerId));
        log.info("Выполнен запрос createItem");
        return savedItemDto;
    }


    //Изменить можно название, описание и статус доступа к аренде. Редактировать вещь может только её владелец.
    @PatchMapping("/{itemId}")
    public ItemDto patchItem(@PathVariable Long itemId,
                          @Valid @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") Long id) {
        Item oldItem = itemService.getItem(itemId);

        Item newItem = new Item(itemId,
                itemDto.getName() == null? oldItem.getName() : itemDto.getName(),
                itemDto.getDescription() == null? oldItem.getDescription() : itemDto.getDescription(),
                itemDto.getAvailable() == null? oldItem.getAvailable() : itemDto.getAvailable(),
                id,
                null);
        Item savedItem = itemService.patchItem(newItem);
        log.info("Выполнен запрос patchItem");
        return ItemMapper.toItemDto(savedItem);
    }



    //Информацию о вещи может просмотреть любой пользователь. (главное, что заоловок не Null)
    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId,
                           @RequestHeader("X-Sharer-User-Id") Long id) {
        Item foundItem = itemService.getItem(itemId);
        log.info("Выполнен запрос getItem");
        return ItemMapper.toItemDto(foundItem);
    }


    //Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой. Эндпойнт GET /items.
    @GetMapping()
    public Collection<ItemDto> getMyItems(@RequestHeader("X-Sharer-User-Id") Long id) {
        List<Item> itemList = List.copyOf(itemService.getMyItems(id));
        log.info("Выполнен запрос getMyItems");
        return itemList.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    /*
    Поиск вещи потенциальным арендатором. Пользователь передаёт в строке запроса текст, и система ищет вещи,
    содержащие этот текст в названии или описании.
    Происходит по эндпойнту /items/search?text={text}, в text передаётся текст для поиска.
    Проверьте, что поиск возвращает только доступные для аренды вещи.
     */
    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam String text,
                           @RequestHeader("X-Sharer-User-Id") Long id) {

        List<Item> foundItems = new ArrayList<>(itemService.searchItems(text));
        log.info("Выполнен запрос searchItems");
        return foundItems.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }


    /* не используется
    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId) {

    }*/
}
