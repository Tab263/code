package com.restaurant.service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.restaurant.service.entities.ShipAddress;



public interface ShipAddressRepository extends JpaRepository<ShipAddress, Long> {

	@Query("SELECT s FROM ShipAddress s INNER JOIN s.customer c WHERE c.email = ?1 and s.shipDefault = true")
	public ShipAddress findDefaultShipAddrress(String email);
}
