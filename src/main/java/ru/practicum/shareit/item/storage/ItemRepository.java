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
            "where i.name ilike CONCAT('%', ?1, '%') or i.description ilike CONCAT('%', ?1, '%')" +
            "and i.available = true", nativeQuery = true)
    List<Item> searchItemsContainsTextAvailableTrue(@NotNull String text);
}
