package com.kti.restaurant.controller;

import com.kti.restaurant.dto.order.OrderDto;
import com.kti.restaurant.dto.salary.CreateSalaryDto;
import com.kti.restaurant.dto.salary.SalaryDto;
import com.kti.restaurant.mapper.SalaryMapper;
import com.kti.restaurant.model.Salary;
import com.kti.restaurant.service.contract.ISalaryService;
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
@RequestMapping(value = "api/v1/salaries")
public class SalaryController {
    private ISalaryService salaryService;
    private SalaryMapper salaryMapper;

    @Autowired
    public SalaryController(ISalaryService salaryService, SalaryMapper salaryMapper) {
        this.salaryService = salaryService;
        this.salaryMapper = salaryMapper;
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<List<SalaryDto>> getSalaries() {
    	List<SalaryDto> salaries = salaryService.findAll().stream()
                .map(salary -> this.salaryMapper.fromSalarytoSalaryDto(salary)).collect(Collectors.toList());
        return new ResponseEntity<>(salaries, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'COOK', 'BARTENDER', 'WAITER')")
    public ResponseEntity<SalaryDto> getSalary(@PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(salaryMapper.fromSalarytoSalaryDto(salaryService.findById(id)), HttpStatus.OK);
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('MANAGER')")

    public ResponseEntity<SalaryDto> createSalary(@Valid @RequestBody CreateSalaryDto salaryDto) throws Exception {
        SalaryDto salary = salaryMapper.fromSalarytoSalaryDto(salaryService.create(salaryMapper.fromCreateSalaryDtoToSalary(salaryDto)));
        return new ResponseEntity<>(salary, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<SalaryDto> updateSalary(@Valid @RequestBody CreateSalaryDto salaryDto, @PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(salaryMapper.fromSalarytoSalaryDto(salaryService.update(salaryMapper.fromCreateSalaryDtoToSalary(salaryDto), id)),
                HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<?> deleteSalary(@PathVariable Integer id) throws Exception {
        salaryService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
