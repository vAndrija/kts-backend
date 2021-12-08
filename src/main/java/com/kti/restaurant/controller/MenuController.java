package com.kti.restaurant.controller;

import com.kti.restaurant.dto.menu.MenuDto;
import com.kti.restaurant.mapper.MenuMapper;
import com.kti.restaurant.model.Menu;
import com.kti.restaurant.service.contract.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<?> createMenu(@Valid @RequestBody MenuDto menuDto) throws Exception {
        Menu menu = menuService.create(menuMapper.fromMenuDtoToMenu(menuDto));

        if(menu != null) {
            return new ResponseEntity<>(menu, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> getMenuById(@PathVariable Integer id) throws Exception {
        return new ResponseEntity<Menu>(menuService.findById(id), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<Menu>> getMenus() {
        return new ResponseEntity<>(menuService.findAll(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<Menu> updateMenu(@Valid @RequestBody MenuDto menuDto, @PathVariable Integer id) throws Exception {
        return new ResponseEntity<Menu>(menuService.update(menuMapper.fromMenuDtoToMenu(menuDto), id),
                HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<?> deleteMenu(@PathVariable Integer id) throws Exception {
        menuService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
