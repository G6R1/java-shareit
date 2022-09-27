package ru.practicum.shareit.item.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemClient {

    private final WebClient client;
    private final String serverUri;

    public ItemClient(@Value("${shareit-server.url}") String serverUri) {
        this.client = WebClient.create();
        this.serverUri = serverUri;
    }

    public ItemDto createItem(ItemDto itemDto, Long requetorId) {
        return client.post()
                .uri(serverUri + "/items")
                .body(Mono.just(itemDto), ItemDto.class)
                .header("X-Sharer-User-Id", String.valueOf(requetorId))
                .retrieve()
                .bodyToMono(ItemDto.class)
                .block();
    }

    public ItemDto patchItem(Long itemId, ItemDto itemDto, Long requetorId) {
        return client.patch()
                .uri(serverUri + "/items/" + itemId)
                .body(Mono.just(itemDto), ItemDto.class)
                .header("X-Sharer-User-Id", String.valueOf(requetorId))
                .retrieve()
                .bodyToMono(ItemDto.class)
                .block();
    }

    public ItemDtoForOwner getItem(Long itemId, Long requetorId) {
        return client.get()
                .uri(serverUri + "/items/" + itemId)
                .header("X-Sharer-User-Id", String.valueOf(requetorId))
                .retrieve()
                .bodyToMono(ItemDtoForOwner.class)
                .block();
    }

    public List<ItemDtoForOwner> getMyItems(Long requetorId, Integer from, Integer size) {
        return client.get()
                .uri(serverUri + "/items/"
                        + "?from=" + from
                        + "&size=" + size)
                .header("X-Sharer-User-Id", String.valueOf(requetorId))
                .retrieve()
                .bodyToFlux(ItemDtoForOwner.class)
                .collect(Collectors.toList())
                .block();
    }

    public List<ItemDto> searchItems(String text, Long requetorId, Integer from, Integer size) {
        return client.get()
                .uri(serverUri + "/items/search/"
                        + "?text=" + text
                        + "&from=" + from
                        + "&size=" + size)
                .header("X-Sharer-User-Id", String.valueOf(requetorId))
                .retrieve()
                .bodyToFlux(ItemDto.class)
                .collect(Collectors.toList())
                .block();
    }

    public CommentDto createComment(CommentDto commentDto, Long itemId, Long requetorId) {
        return client.post()
                .uri(serverUri + "/items/" + itemId + "/comment")
                .body(Mono.just(commentDto), CommentDto.class)
                .header("X-Sharer-User-Id", String.valueOf(requetorId))
                .retrieve()
                .bodyToMono(CommentDto.class)
                .block();
    }
}
