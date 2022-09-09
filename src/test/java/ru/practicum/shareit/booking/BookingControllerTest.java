package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.service.BookingService;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

//@Transactional
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@WebMvcTest(controllers = BookingController.class)
//@MockBean
@AutoConfigureMockMvc
class BookingControllerTest {

    @MockBean
    BookingService bookingService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    void createBooking() {
    }

    @Test
    void confirmBooking() {
    }

    @Test
    void getBooking() {
    }

    @Test
    void getAllMyBookings() {
    }

    @Test
    void getAllBookingsForMyItems() {
    }
}