package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j

@RequestMapping("/items")
public class ItemController {

    final private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping()
    public ItemDto createItem(@RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") Long id) {
        ItemDto savedItemDto = ItemMapper
                .toItemDto(itemService
                        .createItem(itemDto, id));
        log.info("Выполнен запрос createItem");
        return savedItemDto;
    }


    //Изменить можно название, описание и статус доступа к аренде. Редактировать вещь может только её владелец.
    @PatchMapping("/{itemId}")
    public ItemDto patchItem(@PathVariable Long itemId,
                             @RequestBody ItemDto itemDto,
                             @RequestHeader("X-Sharer-User-Id") Long id) {

        Item savedItem = itemService.patchItem(itemId, itemDto, id);
        log.info("Выполнен запрос patchItem");
        return ItemMapper.toItemDto(savedItem);
    }


    //Информацию о вещи может просмотреть любой пользователь.
    @GetMapping("/{itemId}")
    public ItemDtoForOwner getItem(@PathVariable Long itemId,
                                   @RequestHeader("X-Sharer-User-Id") Long id) {

        ItemDtoForOwner foundItem = itemService.getItemWithOwnerCheck(itemId, id);
        log.info("Выполнен запрос getItem");
        return foundItem;
    }


    //Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой. Эндпойнт GET /items.
    @GetMapping()
    public Collection<ItemDtoForOwner> getMyItems(@RequestHeader("X-Sharer-User-Id") Long id,
                                                  @RequestParam(required = false, defaultValue = "0") Integer from,
                                                  @RequestParam(required = false, defaultValue = "100") Integer size) {
        List<ItemDtoForOwner> itemList = itemService.getMyItems(id, from, size);
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
    public Collection<ItemDto> searchItems(@RequestParam String text,
                                           @RequestHeader("X-Sharer-User-Id") Long id,
                                           @RequestParam(required = false, defaultValue = "0") Integer from,
                                           @RequestParam(required = false, defaultValue = "100") Integer size) {

        List<Item> foundItems = itemService.searchItems(text, from, size);
        log.info("Выполнен запрос searchItems");
        return foundItems.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }


    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestBody CommentDto commentDto,
                                    @PathVariable Long itemId,
                                    @RequestHeader("X-Sharer-User-Id") Long id) {

        Comment comment = itemService.createComment(CommentMapper.toComment(commentDto, null, null),
                itemId, id);
        log.info("Выполнен запрос createComment");
        return CommentMapper.toCommentDto(comment);
    }
}
