package com.restaurant.service.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restaurant.service.entities.Income;
@Repository
public interface IncomeRepository extends JpaRepository<Income, Long>{
	  List<Income> findAll();
	  Income findByDay(LocalDate day);
	  List<Income> findByDayBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
