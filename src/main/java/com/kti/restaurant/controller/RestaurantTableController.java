package com.kti.restaurant.controller;

import com.kti.restaurant.model.RestaurantTable;
import com.kti.restaurant.service.contract.IRestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/restaurant-table")
public class RestaurantTableController {
    private IRestaurantTableService restaurantTableService;

    @Autowired
    RestaurantTableController(IRestaurantTableService restaurantTableService) {
        this.restaurantTableService = restaurantTableService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantTable> getRestaurantTable(@PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(restaurantTableService.findById(id), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<RestaurantTable>> getRestaurantTables() {
        return new ResponseEntity<>(restaurantTableService.findAll(), HttpStatus.OK);
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('MANAGER', 'SYSTEM_ADMIN')")
    public ResponseEntity<RestaurantTable> createRestaurantTable(@RequestBody RestaurantTable restaurantTable) throws Exception {
        RestaurantTable restaurantTableToCreate = restaurantTableService.create(restaurantTable);

        if(restaurantTableToCreate != null) {
            return new ResponseEntity<>(restaurantTableToCreate, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'SYSTEM_ADMIN')")
    public ResponseEntity<RestaurantTable> updateRestaurantTable(@RequestBody RestaurantTable restaurantTable, @PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(restaurantTableService.update(restaurantTable, id), HttpStatus.OK);
    }

    @RequestMapping(value="{id}", method=RequestMethod.DELETE)
    @PreAuthorize("hasAnyRole('MANAGER', 'SYSTEM_ADMIN')")
    public ResponseEntity<?> deleteRestaurantTable(@PathVariable Integer id) throws Exception {
        restaurantTableService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
