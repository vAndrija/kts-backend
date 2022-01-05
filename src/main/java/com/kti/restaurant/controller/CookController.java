package com.kti.restaurant.controller;


import com.kti.restaurant.dto.cook.CookCreateDto;
import com.kti.restaurant.dto.cook.CookDto;
import com.kti.restaurant.dto.cook.CookUpdateDto;
import com.kti.restaurant.mapper.CookMapper;
import com.kti.restaurant.model.Cook;
import com.kti.restaurant.model.User;
import com.kti.restaurant.service.contract.ICookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/cook")
public class CookController {

    private ICookService cookService;
    private CookMapper cookMapper;

    @Autowired
    public CookController(ICookService cookService, CookMapper cookMapper) {
        this.cookService = cookService;
        this.cookMapper = cookMapper;
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN')")
    public ResponseEntity<?> createCook(@Valid @RequestBody CookCreateDto cookCreateDto) throws Exception {
        Cook cook = cookService.create(cookMapper.fromCookCreateDtoToCook(cookCreateDto));
        return new ResponseEntity<>(cookMapper.fromCookToCookDto(cook), HttpStatus.CREATED);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'MANAGER')")
    public ResponseEntity<?> getCooks() {
        List<CookDto> bartenderDtos = cookService.findAll().stream()
                .map(cook -> this.cookMapper.fromCookToCookDto(cook)).collect(Collectors.toList());
        return new ResponseEntity<>(bartenderDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'MANAGER', 'COOK')")
    public ResponseEntity<?> getCook(@PathVariable Integer id) throws Exception {
        Cook cook = cookService.findById(id);
        return new ResponseEntity<>(cookMapper.fromCookToCookDto(cook), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'MANAGER', 'COOK')")
    public ResponseEntity<?> updateCook(@Valid @RequestBody CookUpdateDto cookUpdateDto, @PathVariable Integer id) throws Exception {
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user.getRoles().get(0).getId()==3L && !user.getId().equals(id))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Cook cook = cookService.update(cookMapper.fromCookUpdateDtoToCook(cookUpdateDto), id);
        return new ResponseEntity<>(cookMapper.fromCookToCookDto(cook), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'MANAGER')")
    public ResponseEntity<?> deleteCook(@PathVariable Integer id) throws Exception {
        cookService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/updatePriority/{id}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<?> updatePriority(@PathVariable Integer id )throws Exception{
        cookService.updatePriority(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
