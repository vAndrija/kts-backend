package com.kti.restaurant.controller;

import com.kti.restaurant.dto.menuitem.CreateMenuItemDto;
import com.kti.restaurant.dto.menuitem.UpdateMenuItemDto;
import com.kti.restaurant.mapper.MenuItemMapper;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.service.contract.IMenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/menu-items")
public class MenuItemController {

    private IMenuItemService menuItemService;
    private MenuItemMapper menuItemMapper;

    @Autowired
    MenuItemController(IMenuItemService menuItemService, MenuItemMapper menuItemMapper) {
        this.menuItemService = menuItemService;
        this.menuItemMapper = menuItemMapper;
    }

    @PostMapping("")
    public ResponseEntity<?> createMenuItem(@RequestBody CreateMenuItemDto menuItemDto) {
        try {
            MenuItem menuItem = menuItemService.create(menuItemMapper.fromCreateMenuItemDtoToMenuItem(menuItemDto));

            if(menuItem != null) {
                return new ResponseEntity<>(menuItem, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Integer id) {
        return new ResponseEntity<>(menuItemService.findById(id), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<MenuItem>> getMenuItems() {
        return new ResponseEntity<>((List<MenuItem>) menuItemService.findAll(), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<MenuItem> updateMenuItem(@RequestBody UpdateMenuItemDto updateMenuItemDto) {
        try {
            return new ResponseEntity<>(menuItemService.update(menuItemMapper.fromUpdateMenuItemDtoToMenuItem(updateMenuItemDto)),
                    HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteMenuItem(@PathVariable Integer id) {
        menuItemService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
