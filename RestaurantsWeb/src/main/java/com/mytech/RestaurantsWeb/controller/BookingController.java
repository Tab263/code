package com.mytech.RestaurantsWeb.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mytech.RestaurantsWeb.utils.DateTimeConverterUtil;
import com.restaurant.service.entities.BookingTable;
import com.restaurant.service.services.ReservationService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/booktable")
public class BookingController {
	@Autowired
	private ReservationService reservationService;

	@GetMapping("/add")
	public String viewCreate(Model model) {
		model.addAttribute("bookingTable", new BookingTable());
		return "bookingtable";
	}

	@PostMapping("/add")
	public String create(@Valid @ModelAttribute("bookingTable") BookingTable bookingTable, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			return "bookingtable";
		}
		LocalDateTime dateTime = DateTimeConverterUtil.convertStringToDateTime(bookingTable.getDate(),
				bookingTable.getTime());
		LocalDateTime now = LocalDateTime.now();
		System.out.println("LocalDateTime: " + now);
		if (dateTime.isBefore(now.plusHours(1))) {
			model.addAttribute("error", "Table reservation time must be 1 hours more than the current time");
		} else {
			reservationService.save(bookingTable);
			model.addAttribute("message",
					"Successfully submitted, we will review and contact you, please keep an eye on your gmail inbox");
		}
		return "bookingtable";
	}
}
