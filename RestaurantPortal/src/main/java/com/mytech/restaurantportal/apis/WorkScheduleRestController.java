package com.mytech.restaurantportal.apis;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.service.dtos.StaffScheduleDTO;
import com.restaurant.service.dtos.WorkScheduleDTO;
import com.restaurant.service.entities.StaffSchedule;
import com.restaurant.service.entities.WorkSchedule;
import com.restaurant.service.services.WorkScheduleService;

@RestController
@RequestMapping("/apis/v1/workschedules")
public class WorkScheduleRestController {
	 @Autowired
	    private WorkScheduleService workScheduleService;

	 @GetMapping("")
	 public ResponseEntity<List<WorkScheduleDTO>> getAllWorkSchedules() {
	     List<WorkSchedule> workSchedules = workScheduleService.findAllWorkSchedules();
	     List<WorkScheduleDTO> dtoList = workSchedules.stream()
	             .map(workSchedule -> {
	                 WorkScheduleDTO dto = new WorkScheduleDTO();
	                 dto.setId(workSchedule.getId());
	                 dto.setUserId(workSchedule.getUser().getId());
	                 dto.setUserName(workSchedule.getUser().getFullName()); 

	                 dto.setWorkDate(workSchedule.getWorkDate());
	                 dto.setStartTime(workSchedule.getStartTime());
	                 dto.setEndTime(workSchedule.getEndTime());
	                 
	                 dto.setStartTime1(workSchedule.getStartTime1());
	                 dto.setEndTime1(workSchedule.getEndTime1());
	                 dto.setNote(workSchedule.getNote());
	                 return dto;
	             })
	             .collect(Collectors.toList());
	     return ResponseEntity.ok(dtoList);
	 }
	 

	
		


}
