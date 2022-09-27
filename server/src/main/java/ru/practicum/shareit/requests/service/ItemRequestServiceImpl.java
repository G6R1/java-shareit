package ru.practicum.shareit.requests.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.ItemRequestMapper;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, UserService userService, ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userService = userService;
        this.itemRepository = itemRepository;
    }


    @Override
    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long creatorId) {
        User requestor = userService.getUser(creatorId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, requestor);
        itemRequest.setCreated(LocalDateTime.now());
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getMyItemRequests(Long requestorId) {
        //проверка корректности requestorId
        userService.getUser(requestorId);

        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestor_IdOrderByCreatedDesc(requestorId);


        return toItemRequestDtos(itemRequests);
    }

    @Override
    public List<ItemRequestDto> getItemRequests(Integer from, Integer size, Long requestorId) {
        //проверка корректности requestorId
        userService.getUser(requestorId);

        List<ItemRequest> itemRequests = itemRequestRepository.findPageNotMyRequests(requestorId, from, size);

        return toItemRequestDtos(itemRequests);
    }

    private List<ItemRequestDto> toItemRequestDtos(List<ItemRequest> itemRequests) {
        List<ItemRequestDto> itemRequestsDto = itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());

        itemRequestsDto.forEach(x -> {
            x.setItems(itemRepository.findAllByRequest_Id(x.getId()).stream()
                    .map(ItemMapper::toItemDtoForItemRequest)
                    .collect(Collectors.toList()));
        });

        return itemRequestsDto;
    }

    @Override
    public ItemRequestDto getItemRequest(Long requestId, Long requestorId) {
        //проверка корректности requestorId
        userService.getUser(requestorId);

        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(NotFoundException::new);

        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setItems(itemRepository.findAllByRequest_Id(itemRequestDto.getId()).stream()
                .map(ItemMapper::toItemDtoForItemRequest)
                .collect(Collectors.toList()));

        return itemRequestDto;
    }
}
