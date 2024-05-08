package com.restaurant.service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.restaurant.service.entities.BookingTable;

@Repository
public interface BookingTableRepository extends JpaRepository<BookingTable, Long> {

	@Query("SELECT b FROM BookingTable b WHERE " +
           "(:name IS NULL OR b.name LIKE %:name%) " +
           "AND (:date IS NULL OR b.date LIKE %:date%) " +
           "AND (:time IS NULL OR b.time LIKE %:time%) " +
           "AND (:phoneNumber IS NULL OR b.phoneNumber LIKE %:phoneNumber%) " +
           "AND (:person_number IS NULL OR b.person_number LIKE %:person_number%) " +
           "AND (:special IS NULL OR b.special = :special) "+
		   "AND (:status IS NULL OR b.status = :status)")
    List<BookingTable> search(@Param("name") String name,
                              @Param("date") String date,
                              @Param("time") String time,
                              @Param("phoneNumber") String phoneNumber,
                              @Param("person_number") String person_number,
                              @Param("special") Integer special,
                              @Param("status") Integer status);
}