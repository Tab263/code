package com.restaurant.service.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaurant.service.entities.StaffSchedule;
import com.restaurant.service.entities.WorkSchedule;
import com.restaurant.service.repositories.WorkScheduleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class WorkScheduleService {
	 @Autowired
	    private WorkScheduleRepository workScheduleRepository;
	 @Autowired
	    private WorkStaffScheduleRepository workStaffScheduleRepository;

	    public void saveWorkSchedules(List<WorkSchedule> workSchedules) {
	        workScheduleRepository.saveAll(workSchedules);
	    }
	    
	    public List<WorkSchedule> saveAll(List<WorkSchedule> workSchedules) {
	        return workScheduleRepository.saveAll(workSchedules);
	    }
	    
	    public List<StaffSchedule> saveStaff(List<StaffSchedule > workSchedules) {
	        return  workStaffScheduleRepository.saveAll(workSchedules);
	    }
	    
	    
	    public List<WorkSchedule> findAllWorkSchedules() {
	        return workScheduleRepository.findAllSortedByDate();	   

	    }
	    
	    public List<StaffSchedule> findAllStaffWorkSchedules() {
	        return workStaffScheduleRepository.findAllSortedByDate();	   

	    }
	    
	    public Map<String, List<WorkSchedule>> findWorkSchedulesGroupedByDate() {
	        Map<String, List<WorkSchedule>> workSchedulesMap = new HashMap<>();
	        List<String> distinctDates = workScheduleRepository.findDistinctWorkDates();
	        
	        for (String date : distinctDates) {
	            List<WorkSchedule> schedulesForDate = workScheduleRepository.findByWorkDateOrderByStartTimeAsc(date);
	            workSchedulesMap.put(date, schedulesForDate);
	        }
	        
	        return workSchedulesMap;
	    }
	    
	    public Map<String, List<StaffSchedule>> findWorkSchedulesStaffGroupedByDate() {
	        Map<String, List<StaffSchedule>> workSchedulesStaffMap = new HashMap<>();
	        List<String> distinctDates = workStaffScheduleRepository.findDistinctWorkDates();
	        
	        for (String date : distinctDates) {
	            List<StaffSchedule> schedulesForDate =  workStaffScheduleRepository.findByWorkDateOrderByStartTimeAsc(date);
	            workSchedulesStaffMap.put(date, schedulesForDate);
	        }
	        
	        return workSchedulesStaffMap;
	    }
	    

	    public List<WorkSchedule> findWorkSchedulesByDate(String date) {
	        return workScheduleRepository.findByWorkDate(date);
	    }
	    
	    
	  
	    
}
