package ru.practicum.shareit.booking.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase
class BookingRepositoryTest {

    @Test
    void findAllByBooker_IdOrderByStartDesc() {
    }

    @Test
    void findAllByStatusAndBooker_IdOrderByStartDesc() {
    }

    @Test
    void findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc() {
    }

    @Test
    void findAllByBooker_IdAndStartBeforeAndEndBeforeOrderByStartDesc() {
    }

    @Test
    void findAllByBooker_IdAndStartAfterAndEndAfterOrderByStartDesc() {
    }

    @Test
    void findUserItemsBookingsIds() {
    }

    @Test
    void findAllByItem_IdOrderByStartDesc() {
    }
}