package ru.practicum.shareit.requests;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.service.ItemRequestService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }


    /**
     * POST /requests — добавить новый запрос вещи. Основная часть запроса — текст запроса,
     * где пользователь описывает, какая именно вещь ему нужна.
     *
     * @param itemRequestDto -
     * @param id             -
     * @return -
     */
    @PostMapping()
    public ItemRequestDto createItemRequest(@RequestBody ItemRequestDto itemRequestDto,
                                            @RequestHeader("X-Sharer-User-Id") Long id) {

        ItemRequestDto savedRequest = itemRequestService.createItemRequest(itemRequestDto, id);
        log.info("Выполнен запрос createItemRequest");
        return savedRequest;
    }

    /**
     * GET /requests — получить список своих запросов вместе с данными об ответах на них.
     * Для каждого запроса должны указываться описание, дата и время создания и список ответов в формате:
     * id вещи, название, id владельца. Так в дальнейшем, используя указанные id вещей, можно будет получить
     * подробную информацию о каждой вещи. Запросы должны возвращаться в отсортированном порядке от более новых к более старым.
     *
     * @param id -
     * @return -
     */
    @GetMapping()
    public List<ItemRequestDto> getMyItemRequests(@RequestHeader("X-Sharer-User-Id") Long id) {

        List<ItemRequestDto> itemRequests = itemRequestService.getMyItemRequests(id);
        log.info("Выполнен запрос getMyItemRequests");
        return itemRequests;
    }

    /**
     * GET /requests/all?from={from}&size={size} — получить список запросов, созданных другими пользователями.
     * С помощью этого эндпоинта пользователи смогут просматривать существующие запросы, на которые они могли бы ответить.
     * Запросы сортируются по дате создания: от более новых к более старым. Результаты должны возвращаться постранично.
     *
     * @param from индекс первого элемента, начиная с 0
     * @param size количество элементов для отображения.
     * @param id   -
     * @return -
     */
    @GetMapping("/all")
    public List<ItemRequestDto> getItemRequests(@RequestParam(required = false, defaultValue = "0") Integer from,
                                                @RequestParam(required = false, defaultValue = "100") Integer size,
                                                @RequestHeader("X-Sharer-User-Id") Long id) {

        List<ItemRequestDto> itemRequests = itemRequestService.getItemRequests(from, size, id);
        log.info("Выполнен запрос getItemRequests");
        return itemRequests;
    }

    /**
     * GET /requests/{requestId} — получить данные об одном конкретном запросе вместе с данными об ответах
     * на него в том же формате, что и в эндпоинте GET /requests.
     * Посмотреть данные об отдельном запросе может любой пользователь.
     *
     * @param requestId -
     * @param id        -
     * @return -
     */
    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequest(@PathVariable Long requestId,
                                         @RequestHeader("X-Sharer-User-Id") Long id) {

        ItemRequestDto itemRequestDto = itemRequestService.getItemRequest(requestId, id);
        log.info("Выполнен запрос getItemRequest");
        return itemRequestDto;
    }
}
