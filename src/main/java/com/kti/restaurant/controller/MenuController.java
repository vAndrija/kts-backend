package com.kti.restaurant.controller;

import com.kti.restaurant.dto.menu.CreateMenuDto;
import com.kti.restaurant.dto.menu.UpdateMenuDto;
import com.kti.restaurant.mapper.MenuMapper;
import com.kti.restaurant.model.Menu;
import com.kti.restaurant.service.contract.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/menu")
public class MenuController {
    private IMenuService menuService;
    private MenuMapper menuMapper;

    @Autowired
    MenuController(IMenuService menuService, MenuMapper menuMapper) {
        this.menuService = menuService;
        this.menuMapper = menuMapper;
    }

    @PostMapping("")
    public ResponseEntity<?> createMenu(@Valid @RequestBody CreateMenuDto menuDto) throws Exception {
        Menu menu = menuService.create(menuMapper.fromCreateMenuDtoToMenu(menuDto));

        if(menu != null) {
            return new ResponseEntity<>(menu, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> getMenuItemById(@PathVariable Integer id) throws Exception {
        return new ResponseEntity<Menu>(menuService.findById(id), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<Menu>> getMenuItems() {
        return new ResponseEntity<>((List<Menu>) menuService.findAll(), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<Menu> updateMenuItem(@Valid @RequestBody UpdateMenuDto updateMenuDto) throws Exception {
        return new ResponseEntity<Menu>(menuService.update(menuMapper.fromUpdateMenuDtoToMenu(updateMenuDto)),
                HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteMenuItem(@PathVariable Integer id) throws Exception {
        menuService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
