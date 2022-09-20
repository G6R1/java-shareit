package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwner_Id(@NotNull Long ownerId);

    @Query(value = "select * " +
            "from items as i " +
            "where i.owner_id = ?1 " +
            "ORDER BY i.item_id " +
            "LIMIT ?3 OFFSET ?2", nativeQuery = true)
    List<Item> findPageByOwner_Id(@NotNull Long ownerId, @NotNull Integer from, @NotNull Integer size) ;

    @Query(value = "select * " +
            "from items as i " +
            "where i.name ilike CONCAT('%', ?1, '%') or i.description ilike CONCAT('%', ?1, '%') " +
            "and i.available = true", nativeQuery = true)
    List<Item> searchItemsContainsTextAvailableTrue(@NotNull String text);

    @Query(value = "select * " +
            "from items as i " +
            "where i.name ilike CONCAT('%', ?1, '%') or i.description ilike CONCAT('%', ?1, '%') " +
            "and i.available = true " +
            "LIMIT ?3 OFFSET ?2", nativeQuery = true)
    List<Item> searchItemsPageContainsTextAvailableTrue(@NotNull String text,
                                                               @NotNull Integer from,
                                                               @NotNull Integer size) ;

    List<Item> findAllByRequest_Id(Long requestId);
}
