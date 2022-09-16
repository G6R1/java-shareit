package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.List;


public interface ItemRequestService {

    ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long creatorId) ;

    List<ItemRequestDto> getMyItemRequests(Long requestorId) ;

    List<ItemRequestDto> getItemRequests(Integer from, Integer size, Long requestorId) ;

    ItemRequestDto getItemRequest(Long requestId, Long requestorId) ;
}
