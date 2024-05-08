package com.restaurant.service.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.restaurant.service.entities.Ingredient;
import com.restaurant.service.entities.RestaurantTable;
import com.restaurant.service.entities.Role;

@Repository
public interface RestaurantTableRespository extends CrudRepository<RestaurantTable, Long> {
	 @Query("SELECT i FROM RestaurantTable i JOIN FETCH i.category")
	    List<RestaurantTable> findAllWithCategoryAndUnit();
	 
	  Optional<RestaurantTable> findByTableNumber(String tableNumber);
	  boolean existsByTableNumber(String tableNumber);

	  
}
