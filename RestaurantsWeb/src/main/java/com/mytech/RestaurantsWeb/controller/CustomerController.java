package com.mytech.RestaurantsWeb.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mytech.RestaurantsWeb.security.AppUserDetails;
import com.nimbusds.jose.shaded.gson.Gson;
import com.restaurant.service.dtos.StaffScheduleDTO;
import com.restaurant.service.dtos.WorkScheduleDTO;
import com.restaurant.service.entities.StaffSchedule;
import com.restaurant.service.entities.WorkSchedule;
import com.restaurant.service.repositories.WorkScheduleRepository;
import com.restaurant.service.services.CartService;
import com.restaurant.service.services.WorkStaffScheduleRepository;

@Controller

public class CustomerController {
	 @Autowired
	    private CartService cartService;
	    @Autowired
	  	private WorkStaffScheduleRepository workStaffScheduleRepository ;
	    
	    
	    @GetMapping("/listwork")
		public String liststaff(Model model, @AuthenticationPrincipal AppUserDetails loggedUser) {
		    Long customerId= loggedUser.getId();
		    List<StaffSchedule> works = workStaffScheduleRepository.findByCustomerIdWithCategory(customerId);
		   
		    model.addAttribute("works", works);
		    model.addAttribute("currentPage", "1");
		    model.addAttribute("sortField", "email");
		    model.addAttribute("sortDir", "asc");
		    model.addAttribute("reverseSortDir", "desc");
		    model.addAttribute("searchText", "");
		    model.addAttribute("moduleURL", "/add");

		    return "listwork";
		}
	    
	    
	    @GetMapping("/calendar")
	    public String listStaff(Model model, @AuthenticationPrincipal AppUserDetails loggedUser) {
	        Long customerId = loggedUser.getId();
	        List<StaffSchedule> works = workStaffScheduleRepository.findByCustomerIdOrderByWorkDateAsc(customerId);
	       
	      
	        List<StaffScheduleDTO> worksDTO = new ArrayList<>();
	        for (StaffSchedule work : works) {
	        	StaffScheduleDTO dto = new StaffScheduleDTO();
	            dto.setId(work.getId());
	            dto.setWorkDate(work.getWorkDate());
	            dto.setStartTime(work.getStartTime());
	            dto.setEndTime(work.getEndTime());
	            dto.setStartTime1(work.getStartTime1());
	            dto.setEndTime1(work.getEndTime1());
	            dto.setNote(work.getNote());
	            worksDTO.add(dto);
	        }

	       
	        Gson gson = new Gson();
	        String worksJson = gson.toJson(worksDTO);
	       
	      
	        model.addAttribute("worksJson", worksJson); 

	        return "calendar";
	    }
}
