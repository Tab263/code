package com.restaurant.service.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.restaurant.service.entities.TCategory;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantTableDTO {

    private String tableNumber;
    private boolean status;
    private int seatCount;
    private TCategory category;
  

    public RestaurantTableDTO(String tableNumber, boolean status, int seatCount, TCategory category) {
        this.tableNumber = tableNumber;
        this.status = status;
        this.seatCount = seatCount;
        this.category = category;
        
    }

    public RestaurantTableDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	// Getters and setters
    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public TCategory getCategory() {
        return category;
    }

    public void setCategory(TCategory category) {
        this.category = category;
    }

    
}
