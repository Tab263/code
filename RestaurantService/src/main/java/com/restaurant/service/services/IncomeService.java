package com.restaurant.service.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaurant.service.entities.Income;
import com.restaurant.service.entities.IncomeItem;
import com.restaurant.service.repositories.IncomeRepository;

@Service
public class IncomeService {
	 @Autowired
	 private IncomeRepository incomeRepository;

	    public List<Income> getAllIncomes() {
	        return incomeRepository.findAll();
	    }

	    public void updateSoldQuantityForAllIncomeItems(Income income) {
	        for (IncomeItem incomeItem : income.getIncomeItems()) {
	            incomeItem.calculateSoldQuantity();
	        }
	    }
	    public void save(Income income) {
	    	incomeRepository.save(income);
	    }
	  
	    public Income findByDay(LocalDate day) {
	        return incomeRepository.findByDay(day);
	    }
	    public List<Income> findByDayBetween(LocalDateTime startOfDay, LocalDateTime endOfDay) {
	        return incomeRepository.findByDayBetween(startOfDay, endOfDay);
	    }
	    
	  
}
