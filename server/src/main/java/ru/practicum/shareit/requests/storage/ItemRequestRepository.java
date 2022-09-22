package ru.practicum.shareit.requests.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequestor_IdOrderByCreatedDesc(Long requestorId);


    @Query(value = "select * " +
            "from requests as r " +
            "where r.requestor_id <> ?1 " +
            "ORDER BY r.created DESC " +
            "LIMIT ?3 OFFSET ?2", nativeQuery = true)
    List<ItemRequest> findPageNotMyRequests(Long requestorId, Integer from, Integer size);

    @Query(value = "select * " +
            "from requests as r " +
            "where r.requestor_id <> ?1 " +
            "ORDER BY r.created DESC", nativeQuery = true)
    List<ItemRequest> findAllNotMyRequests(Long requestorId);
}
