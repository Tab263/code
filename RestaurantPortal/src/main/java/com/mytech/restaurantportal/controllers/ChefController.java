package com.mytech.restaurantportal.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mytech.restaurantportal.security.AppUserDetails;
import com.nimbusds.jose.shaded.gson.Gson;
import com.restaurant.service.dtos.WorkScheduleDTO;
import com.restaurant.service.entities.CartLine;
import com.restaurant.service.entities.WorkSchedule;
import com.restaurant.service.enums.OrderStatus;
import com.restaurant.service.repositories.WorkScheduleRepository;
import com.restaurant.service.services.CartService;

@Controller
@RequestMapping("/chef")
public class ChefController {
    @Autowired
    private CartService cartService;
    @Autowired
  	private WorkScheduleRepository workScheduleRepository ;
    
    
   
    @GetMapping("/schedule")
    public String listChef(Model model, @AuthenticationPrincipal AppUserDetails loggedUser) {
        Long userId = loggedUser.getId();
        List<WorkSchedule> works = workScheduleRepository.findByUserIdOrderByWorkDateAsc(userId);
       
      
        List<WorkScheduleDTO> worksDTO = new ArrayList<>();
        for (WorkSchedule work : works) {
            WorkScheduleDTO dto = new WorkScheduleDTO();
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

        return "/apps/chef/schedule";
    }

        
    @GetMapping("/list")
    public String viewMenuPage(Model model) {
        List<CartLine> cartLines = cartService.listAllCartLines();
        model.addAttribute("cartLines", cartLines); // Already sorted by recent orderTime
        return "/apps/chef/list";
    }

    
    @PostMapping("/serveDishes")
    @ResponseBody
    public ResponseEntity<String> serveDishes(@RequestParam("dishIds[]") List<Long> dishIds, @RequestParam("status") OrderStatus status) {
        try {
            for (Long dishId : dishIds) {
                cartService.updateCartLineStatus(dishId, status);
            }
            return new ResponseEntity<>("Dish status updated successfully.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
   
    @PostMapping("/updateStatus")
    @ResponseBody
    public ResponseEntity<?> updateStatus(@RequestParam("cartLineId") Long cartLineId, @RequestParam("status") String status) {
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status); // Chuyển đổi String sang enum
            cartService.updateCartLineStatus(cartLineId, orderStatus);
            return ResponseEntity.ok(Map.of("message", "Dish status updated successfully."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An error occurred: " + e.getMessage()));
        }
     
        
        
    }





    
   
    


    

}

