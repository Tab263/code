package com.restaurant.service.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "restaurant_table")
public class RestaurantTable extends AbstractEntity  {

   

    private static final long serialVersionUID = 1L;

	@NotNull
    @Column(name = "table_number")
    private String tableNumber;

	@Column(columnDefinition = "tinyint(1) default 1")
    private boolean status;

    @Column(name = "seat_count")
    private int seatCount;
    
  
	

	public RestaurantTable(@NotNull String tableNumber, boolean status, int seatCount, TCategory category) {
		super();
		this.tableNumber = tableNumber;
		this.status = status;
		this.seatCount = seatCount;
		this.category = category;
	}

	public RestaurantTable() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TCategory getCategory() {
		return category;
	}

	public void setCategory(TCategory category) {
		this.category = category;
	}


	@ManyToOne
	@JoinColumn(name = "category_id")
	private TCategory category;

   

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

	

	
	
    
    
   
}