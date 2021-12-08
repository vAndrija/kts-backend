package com.kti.restaurant.controller;

import com.kti.restaurant.dto.menuitem.MenuItemDto;
import com.kti.restaurant.dto.menuitem.UpdateMenuItemDto;
import com.kti.restaurant.mapper.MenuItemMapper;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.service.contract.IMenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    @PreAuthorize("hasAnyRole('COOK', 'BARTENDER')")
    public ResponseEntity<?> createMenuItem(@Valid @RequestBody MenuItemDto menuItemDto) throws Exception {
        MenuItem menuItem = menuItemService.create(menuItemMapper.fromCreateMenuItemDtoToMenuItem(menuItemDto));

        if(menuItem != null) {
            return new ResponseEntity<>(menuItem, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(menuItemService.findById(id), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<MenuItem>> getMenuItems() {
        return new ResponseEntity<>(menuItemService.findAll(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('COOK', 'BARTENDER', 'MANAGER')")
    public ResponseEntity<?> updateMenuItem(@Valid @RequestBody UpdateMenuItemDto updateMenuItemDto, @PathVariable Integer id) throws Exception {
            return new ResponseEntity<>(menuItemService.update(menuItemMapper.fromUpdateMenuItemDtoToMenuItem(updateMenuItemDto), id),
                    HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    @PreAuthorize("hasAnyRole('COOK', 'BARTENDER', 'MANAGER')")
    public ResponseEntity<?> deleteMenuItem(@PathVariable Integer id) throws Exception {
        menuItemService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/search/{search}")
    public ResponseEntity<?> searchMenuItems(@PathVariable("search") String s) {
        List<MenuItemDto> menuItems = menuItemService.search(s).stream()
                .map(menuItem->this.menuItemMapper.fromMenuItemToMenuItemDto(menuItem)).collect(Collectors.toList());
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    @GetMapping(value = "/filter/{filter}")
    public ResponseEntity<?> filterMenuItems(@PathVariable("filter") String f) {
        List<MenuItemDto> menuItems = menuItemService.filter(f).stream()
                .map(menuItem->this.menuItemMapper.fromMenuItemToMenuItemDto(menuItem)).collect(Collectors.toList());
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }
}
