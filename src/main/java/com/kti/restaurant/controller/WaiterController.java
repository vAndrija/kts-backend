package com.kti.restaurant.controller;


import com.kti.restaurant.dto.waiter.WaiterCreateDto;
import com.kti.restaurant.dto.waiter.WaiterDto;
import com.kti.restaurant.dto.waiter.WaiterUpdateDto;
import com.kti.restaurant.mapper.WaiterMapper;
import com.kti.restaurant.model.User;
import com.kti.restaurant.model.Waiter;
import com.kti.restaurant.service.contract.IWaiterService;
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
@RequestMapping(value = "/api/v1/waiter")
public class WaiterController {

    private IWaiterService waiterService;
    private WaiterMapper waiterMapper;

    @Autowired
    public WaiterController(IWaiterService waiterService, WaiterMapper waiterMapper) {
        this.waiterService = waiterService;
        this.waiterMapper = waiterMapper;
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN')")
    public ResponseEntity<?> createWaiter(@Valid @RequestBody WaiterCreateDto waiterCreateDto) throws Exception {
        Waiter waiter = waiterService.create(waiterMapper.fromWaiterCreateDtoToWaiter(waiterCreateDto));
        return new ResponseEntity<>(waiterMapper.fromWaiterToWaiterDto(waiter), HttpStatus.CREATED);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('MANAGER', 'SYSTEM_ADMIN')")
    public ResponseEntity<?> getWaiters() {
        List<WaiterDto> waiterDtos = waiterService.findAll().stream()
                .map(waiter -> this.waiterMapper.fromWaiterToWaiterDto(waiter)).collect(Collectors.toList());
        return new ResponseEntity<>(waiterDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'SYSTEM_ADMIN', 'WAITER')")
    public ResponseEntity<?> getWaiter(@PathVariable Integer id) throws Exception {
        Waiter waiter = waiterService.findById(id);
        return new ResponseEntity<>(waiterMapper.fromWaiterToWaiterDto(waiter), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'WAITER')")
    public ResponseEntity<?> updateWaiter(@Valid @RequestBody WaiterUpdateDto waiterUpdateDto, @PathVariable Integer id) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getRoles().get(0).getId() == 5L && !user.getId().equals(id))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Waiter waiter = waiterService.update(waiterMapper.fromWaiterUpdateDtoToWaiter(waiterUpdateDto), id);
        return new ResponseEntity<>(waiterMapper.fromWaiterToWaiterDto(waiter), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'SYSTEM_ADMIN')")
    public ResponseEntity<?> deleteWaiter(@PathVariable Integer id) throws Exception {
        waiterService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
