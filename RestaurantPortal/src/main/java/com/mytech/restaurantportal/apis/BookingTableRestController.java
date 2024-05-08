package com.mytech.restaurantportal.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.restaurant.service.dtos.ContactDTO;
import com.restaurant.service.entities.BookingTable;
import com.restaurant.service.entities.Contact;
import com.restaurant.service.services.ContactService;
import com.restaurant.service.services.ReservationService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/apis/v1/bookingtable")
public class BookingTableRestController {

    @Autowired
    private ReservationService reservationService;
    @PostMapping
    public ResponseEntity<BookingTable> create(@RequestBody BookingTable bookingTable) {
        System.out.println("BookingTable: " + bookingTable.toString());
        BookingTable bookingTable2 = new BookingTable(bookingTable.getName(), bookingTable.getEmail(), bookingTable.getPhoneNumber(), bookingTable.getDate(),bookingTable.getTime(),bookingTable.getPerson_number());
        BookingTable savedBookingTable = reservationService.save(bookingTable2);
        return ResponseEntity.ok(savedBookingTable);
    }
  
}
