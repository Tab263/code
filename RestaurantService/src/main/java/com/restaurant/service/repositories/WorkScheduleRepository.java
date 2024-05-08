package com.restaurant.service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.restaurant.service.entities.WorkSchedule;



public interface WorkScheduleRepository  extends JpaRepository<WorkSchedule, Long>{
	 @Query("SELECT ws FROM WorkSchedule ws ORDER BY ws.workDate ASC")
	    List<WorkSchedule> findAllSortedByDate();
	 
	    List<WorkSchedule> findByWorkDateOrderByStartTimeAsc(String workDate);
	    @Query("SELECT DISTINCT ws.workDate FROM WorkSchedule ws ORDER BY ws.workDate ASC")
	    List<String> findDistinctWorkDates();
	    
	    List<WorkSchedule> findByUserIdOrderByWorkDateAsc(Long userId);
	    
	    List<WorkSchedule> findByWorkDate(String date);




}
