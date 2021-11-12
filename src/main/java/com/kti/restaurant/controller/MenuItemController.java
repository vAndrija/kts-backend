package com.kti.restaurant.controller;

import com.kti.restaurant.dto.MenuItemDto;
import com.kti.restaurant.dto.UpdateMenuItemDto;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/menu-items")
public class MenuItemController {
    @Autowired
    private MenuItemService menuItemService;

    @PostMapping("")
    public ResponseEntity<?> createMenuItem(@RequestBody MenuItemDto menuItemDto) {
        try {
            System.out.println("CAO");

            MenuItem menuItem = menuItemService.create(menuItemDto);
            System.out.println("CAO");

            if(menuItem != null) {
                return new ResponseEntity<>(menuItem, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Integer id) {
        return new ResponseEntity<>(menuItemService.findOne(id), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<MenuItem>> getMenuItems() {
        return new ResponseEntity<>((List<MenuItem>) menuItemService.findAll(), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<MenuItem> updateMenuItem(@RequestBody UpdateMenuItemDto updateMenuItemDto) {
        try {
            return new ResponseEntity<>(menuItemService.update(updateMenuItemDto), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMenuItem(@PathVariable Integer id) {
        menuItemService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
