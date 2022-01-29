package com.kti.restaurant.controller;

import com.kti.restaurant.dto.menuitem.CreateMenuItemDto;
import com.kti.restaurant.dto.menuitem.MenuItemDto;
import com.kti.restaurant.dto.menuitem.UpdateMenuItemDto;
import com.kti.restaurant.mapper.MenuItemMapper;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.service.contract.IMenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
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
    public ResponseEntity<?> createMenuItem(@Valid @RequestBody CreateMenuItemDto menuItemDto) throws Exception {
        MenuItem menuItem = menuItemService.create(menuItemMapper.fromCreateMenuItemDtoToMenuItem(menuItemDto));
        return new ResponseEntity<>(menuItem, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDto> getMenuItemById(@PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(menuItemMapper.fromMenuItemToMenuItemDto(menuItemService.findById(id)), HttpStatus.OK);
    }

    @GetMapping("/pageable")
    public ResponseEntity<?> getMenuItemsPageable(@RequestParam Integer page, @RequestParam Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MenuItemDto> menuItems = menuItemService.findAll(pageable).
                map(menuItem -> this.menuItemMapper.fromMenuItemToMenuItemDto(menuItem));
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<MenuItem>> getMenuItems() {
        return new ResponseEntity<>(menuItemService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/pending-menu-items")
    public ResponseEntity<Page<MenuItem>> getPendingMenuItems(@RequestParam Integer page, @RequestParam Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(menuItemService.pendingMenuItems(pageable), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('COOK', 'BARTENDER', 'MANAGER')")
    public ResponseEntity<?> updateMenuItem(@Valid @RequestBody UpdateMenuItemDto updateMenuItemDto, @PathVariable Integer id) throws Exception {
        MenuItem menuItem = menuItemService.update(menuItemMapper.fromUpdateMenuItemDtoToMenuItem(updateMenuItemDto), id);
        return new ResponseEntity<>(menuItemMapper.fromMenuItemToMenuItemDto(menuItem), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyRole('COOK', 'BARTENDER', 'MANAGER')")
    public ResponseEntity<?> deleteMenuItem(@PathVariable Integer id) throws Exception {
        menuItemService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/search/{search}")
    public ResponseEntity<?> searchMenuItems(@PathVariable("search") String s) {
        List<MenuItemDto> menuItems = menuItemService.search(s, LocalDateTime.now()).stream()
                .map(menuItem -> this.menuItemMapper.fromMenuItemToMenuItemDto(menuItem)).collect(Collectors.toList());
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    @GetMapping(value = "/filter/{filter}")
    public ResponseEntity<?> filterMenuItems(@PathVariable("filter") String f) {
        List<MenuItemDto> menuItems = menuItemService.filter(f, LocalDateTime.now()).stream()
                .map(menuItem -> this.menuItemMapper.fromMenuItemToMenuItemDto(menuItem)).collect(Collectors.toList());
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    @GetMapping(value = "/filter/pageable/{filter}")
    public ResponseEntity<?> filterMenuItemsPageable(@PathVariable("filter") String f, @RequestParam Integer page,
                                                     @RequestParam Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MenuItemDto> menuItems = menuItemService.filterPageable(f, pageable, LocalDateTime.now()).
                map(menuItem -> this.menuItemMapper.fromMenuItemToMenuItemDto(menuItem));
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    @GetMapping(value = "/by-menu/{menuId}")
    public ResponseEntity<Page<MenuItemDto>> findMenuItemsByMenuId(Pageable pageable, @PathVariable Integer menuId) throws Exception {
        Page<MenuItemDto> menuItems = menuItemService.findByMenu(menuId, pageable)
                .map(menuItem -> this.menuItemMapper.fromMenuItemToMenuItemDto(menuItem));
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    @GetMapping(value = "/by-menu/{menuId}/search")
    public ResponseEntity<Page<MenuItemDto>> findMenuItemsByMenuIdAndSearchFilterParams(@RequestParam Integer page, @RequestParam Integer size,
                                                                                        @RequestParam String searchParam, @RequestParam String filter, @PathVariable Integer menuId) throws Exception {
        Pageable pageable = PageRequest.of(page, size);
        Page<MenuItemDto> menuItems = menuItemService.searchAndFilterMenuItems(menuId, searchParam, filter, pageable)
                .map(menuItem -> this.menuItemMapper.fromMenuItemToMenuItemDto(menuItem));
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    @GetMapping("/by-active-menu")
    public ResponseEntity<?> getMenuItemsInActiveMenu(@RequestParam Integer page, @RequestParam Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MenuItemDto> menuItems = menuItemService.findAllInActiveMenu(pageable, LocalDateTime.now()).
                map(menuItem -> this.menuItemMapper.fromMenuItemToMenuItemDto(menuItem));
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }
}
