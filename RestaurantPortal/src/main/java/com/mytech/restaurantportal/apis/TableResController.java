package com.mytech.restaurantportal.apis;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.service.dtos.RestaurantTableDTO;
import com.restaurant.service.entities.RestaurantTable;
import com.restaurant.service.services.RestaurantTableService;


@RestController
@RequestMapping("/apis/v1/tables")
public class TableResController {

    @Autowired
    private RestaurantTableService restaurantTableService;

    @GetMapping("")
    public ResponseEntity<List<RestaurantTableDTO>> getTableList() {
        List<RestaurantTable> listTable = restaurantTableService.getAllRestaurantTables();

        List<RestaurantTableDTO> tableDTOs = listTable.stream().map(table -> {
            RestaurantTableDTO dto = new RestaurantTableDTO(
                    table.getTableNumber(),
                    table.isStatus(),
                    table.getSeatCount(),
                    table.getCategory()
            );
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(tableDTOs);
    }
}