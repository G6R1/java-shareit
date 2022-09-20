package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;

    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @PostMapping()
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") Long id) {
        ItemDto savedItem = itemClient.createItem(itemDto, id);
        log.info("Выполнен запрос createItem");
        return savedItem;
    }


    //Изменить можно название, описание и статус доступа к аренде. Редактировать вещь может только её владелец.
    @PatchMapping("/{itemId}")
    public ItemDto patchItem(@PathVariable Long itemId,
                             @Valid @RequestBody ItemDto itemDto,
                             @RequestHeader("X-Sharer-User-Id") Long id) {
        ItemDto savedItem = itemClient.patchItem(itemId, itemDto, id);
        log.info("Выполнен запрос patchItem");
        return savedItem;
    }


    //Информацию о вещи может просмотреть любой пользователь.
    @GetMapping("/{itemId}")
    public ItemDtoForOwner getItem(@PathVariable Long itemId,
                                   @RequestHeader("X-Sharer-User-Id") Long id) {
        ItemDtoForOwner item = itemClient.getItem(itemId, id);
        log.info("Выполнен запрос getItem");
        return item;
    }


    //Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой. Эндпойнт GET /items.
    @GetMapping()
    public List<ItemDtoForOwner> getMyItems(@RequestHeader("X-Sharer-User-Id") Long id,
                                            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                            @RequestParam(required = false, defaultValue = "100") @Positive Integer size) {
        List<ItemDtoForOwner> itemList = itemClient.getMyItems(id, from, size);
        log.info("Выполнен запрос getMyItems");
        return itemList;
    }

    /*
    Поиск вещи потенциальным арендатором. Пользователь передаёт в строке запроса текст, и система ищет вещи,
    содержащие этот текст в названии или описании.
    Происходит по эндпойнту /items/search?text={text}, в text передаётся текст для поиска.
    Проверьте, что поиск возвращает только доступные для аренды вещи.
     */
    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text,
                                     @RequestHeader("X-Sharer-User-Id") Long id,
                                     @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                     @RequestParam(required = false, defaultValue = "100") @Positive Integer size) {
        List<ItemDto> itemList = itemClient.searchItems(text, id, from, size);
        log.info("Выполнен запрос searchItems");
        return itemList;
    }


    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@Valid @RequestBody CommentDto commentDto,
                                    @PathVariable Long itemId,
                                    @RequestHeader("X-Sharer-User-Id") Long id) {
        CommentDto comment = itemClient.createComment(commentDto, itemId, id);
        log.info("Выполнен запрос createComment");
        return comment;
    }
}
