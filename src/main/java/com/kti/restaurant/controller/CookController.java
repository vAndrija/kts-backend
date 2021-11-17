package com.kti.restaurant.controller;


import com.kti.restaurant.dto.cook.CookCreateDto;
import com.kti.restaurant.dto.cook.CookDto;
import com.kti.restaurant.dto.cook.CookUpdateDto;
import com.kti.restaurant.mapper.CookMapper;
import com.kti.restaurant.model.Cook;
import com.kti.restaurant.service.contract.ICookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> createCook(@RequestBody CookCreateDto cookCreateDto) throws Exception {
        Cook cook = cookService.create(cookMapper.fromCookCreateDtoToCook(cookCreateDto));
        return new ResponseEntity<>(cookMapper.fromCookToCookDto(cook), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<?> getCooks() {
        List<CookDto> bartenderDtos = cookService.findAll().stream()
                .map(cook -> this.cookMapper.fromCookToCookDto(cook)).collect(Collectors.toList());
        return new ResponseEntity<>(bartenderDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCook(@PathVariable Integer id) throws Exception {
        Cook cook = cookService.findById(id);
        return new ResponseEntity<>(cookMapper.fromCookToCookDto(cook), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCook(@RequestBody CookUpdateDto cookUpdateDto, @PathVariable Integer id) throws Exception {
        Cook cook = cookService.update(cookMapper.fromCookUpdateDtoToCook(cookUpdateDto), id);
        return new ResponseEntity<>(cookMapper.fromCookToCookDto(cook), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCook(@PathVariable Integer id) throws Exception {
        cookService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
