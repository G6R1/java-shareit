package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

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
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") Long id) {
        Item itemFromDto = new Item(null,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                id,
                null);
        ItemDto savedItemDto = ItemMapper.toItemDto(itemService.createItem(itemFromDto));
        log.info("Выполнен запрос createUser");
        return savedItemDto;
    }


    //Изменить можно название, описание и статус доступа к аренде. Редактировать вещь может только её владелец.
    @PatchMapping("/{itemId}")
    public ItemDto patchItem(@PathVariable Long itemId,
                          @Valid @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") Long id) {
        Item oldItem = itemService.getItem(itemId);


        User newUser = new User(id,
                userDto.getName() == null? oldUser.getName() : userDto.getName(),
                userDto.getEmail() == null? oldUser.getEmail() : userDto.getEmail());
        User savedUser = userService.patchUser(newUser);
        log.info("Выполнен запрос patchUser");
        return savedUser;
    }



    //Информацию о вещи может просмотреть любой пользователь. (главное, что заоловок не Null)
    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId,
                           @RequestHeader("X-Sharer-User-Id") Long id) {

    }


    //Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой. Эндпойнт GET /items.
    @GetMapping()
    public Collection<ItemDto> getMyItems(@RequestHeader("X-Sharer-User-Id") Long id) {

    }

    /*
    Поиск вещи потенциальным арендатором. Пользователь передаёт в строке запроса текст, и система ищет вещи,
    содержащие этот текст в названии или описании.
    Происходит по эндпойнту /items/search?text={text}, в text передаётся текст для поиска.
    Проверьте, что поиск возвращает только доступные для аренды вещи.
     */
    @GetMapping("/search")
    public ItemDto getItem(@RequestParam String text,
                           @RequestHeader("X-Sharer-User-Id") Long id) {

    }




    /* не используется
    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId) {

    }*/
}
