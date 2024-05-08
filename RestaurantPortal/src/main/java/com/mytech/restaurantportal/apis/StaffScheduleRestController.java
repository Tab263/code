package com.mytech.restaurantportal.apis;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.service.dtos.StaffScheduleDTO;
import com.restaurant.service.entities.StaffSchedule;
import com.restaurant.service.services.WorkScheduleService;

@RestController
@RequestMapping("/apis/v1/staffchedules")
public class StaffScheduleRestController {
	
	 @Autowired
	    private WorkScheduleService workScheduleService;

	    @GetMapping("")
	    public ResponseEntity<Map<String, List<StaffScheduleDTO>>> listStaff() {
	        List<StaffSchedule> workStaffSchedules = workScheduleService.findAllStaffWorkSchedules();
	        
	        Map<String, List<StaffScheduleDTO>> workSchedulesMap = workStaffSchedules.stream()
	                .map(this::convertToStaffScheduleDTO)
	                .collect(Collectors.groupingBy(StaffScheduleDTO::getWorkDate));

	        return ResponseEntity.ok()
	                .body(workSchedulesMap);
	    }

	    private StaffScheduleDTO convertToStaffScheduleDTO(StaffSchedule staffSchedule) {
	        StaffScheduleDTO dto = new StaffScheduleDTO();
	        dto.setId(staffSchedule.getId());
	        dto.setWorkDate(staffSchedule.getWorkDate());
	        dto.setStartTime(staffSchedule.getStartTime());
	        dto.setEndTime(staffSchedule.getEndTime());
	        dto.setStartTime1(staffSchedule.getStartTime1());
	        dto.setEndTime1(staffSchedule.getEndTime1());
	        dto.setNote(staffSchedule.getNote());
	        dto.setCusId(staffSchedule.getCustomer().getId());
	        dto.setCusName(staffSchedule.getCustomer().getFullName());
	        dto.setCategoryId(staffSchedule.getCategory().getId());
	        dto.setCategoryName(staffSchedule.getCategory().getCategoryName());
	        return dto;
	    }

}
