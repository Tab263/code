package com.restaurant.service.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.restaurant.service.entities.StaffSchedule;

public interface WorkStaffScheduleRepository  extends JpaRepository<StaffSchedule, Long>{
	@Query("SELECT ws FROM  StaffSchedule ws ORDER BY ws.workDate ASC")
    List<StaffSchedule> findAllSortedByDate();
 
    List<StaffSchedule> findByWorkDateOrderByStartTimeAsc(String workDate);
    @Query("SELECT DISTINCT ws.workDate FROM  StaffSchedule ws ORDER BY ws.workDate ASC")
    List<String> findDistinctWorkDates();
    
    List<StaffSchedule> findByCustomerIdOrderByWorkDateAsc(Long customerId);
    
    @Query("SELECT s FROM StaffSchedule s JOIN FETCH s.category WHERE s.customer.id = :customerId ORDER BY s.workDate ASC")
    List<StaffSchedule> findByCustomerIdWithCategory(@Param("customerId") Long customerId);

    
}
