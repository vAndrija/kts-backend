package com.kti.restaurant.controller;

import com.kti.restaurant.dto.manager.ManagerCreateDto;
import com.kti.restaurant.dto.manager.ManagerDto;
import com.kti.restaurant.dto.manager.ManagerUpdateDto;
import com.kti.restaurant.mapper.ManagerMapper;
import com.kti.restaurant.model.Manager;
import com.kti.restaurant.service.contract.IManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping(value="/api/v1/manager")
public class ManagerController {

    private IManagerService managerService;
    private ManagerMapper managerMapper;

    @Autowired
    public ManagerController(IManagerService managerService, ManagerMapper managerMapper) {
        this.managerService =  managerService;
        this.managerMapper =  managerMapper;
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN')")
    public ResponseEntity<?> createManger(@Valid @RequestBody ManagerCreateDto managerCreateDto) throws Exception {
        Manager manager =  managerService.create(managerMapper.fromManagerCreateDtoToManger(managerCreateDto));
        return new ResponseEntity<>(managerMapper.fromManagerToManagerDto(manager), HttpStatus.CREATED);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'MANAGER')")
    public ResponseEntity<?> getManagers(){
        List<ManagerDto> managerDtos = managerService.findAll().stream()
                .map(manager->this.managerMapper.fromManagerToManagerDto(manager)).collect(Collectors.toList());
        return new ResponseEntity<>(managerDtos,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'MANAGER')")
    public ResponseEntity<?> getManager(@PathVariable Integer id) throws Exception {
        Manager manager =  managerService.findById(id);
        return new ResponseEntity<>(managerMapper.fromManagerToManagerDto(manager), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'MANAGER')")
    public ResponseEntity<?> updateManager(@Valid @RequestBody ManagerUpdateDto managerUpdateDto, @PathVariable Integer id) throws Exception {
        Manager manager = managerService.update(managerMapper.fromManagerUpdateDtoToManager(managerUpdateDto), id);
        return new ResponseEntity<>(managerMapper.fromManagerToManagerDto(manager),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN')")
    public ResponseEntity<?> deleteManager(@PathVariable Integer id) throws Exception {
        managerService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
