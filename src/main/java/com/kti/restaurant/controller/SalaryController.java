package com.kti.restaurant.controller;

import com.kti.restaurant.dto.salary.CreateSalaryDto;
import com.kti.restaurant.mapper.SalaryMapper;
import com.kti.restaurant.model.Salary;
import com.kti.restaurant.service.contract.ISalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public ResponseEntity<List<Salary>> getSalaries() {
        return new ResponseEntity<>(salaryService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Salary> getSalary(@PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(salaryService.findById(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Salary> createSalary(@RequestBody CreateSalaryDto salaryDto) throws Exception {
        Salary salary = salaryService.create(salaryMapper.fromCreateSalaryDtoToSalary(salaryDto));

        if(salary != null) {
            return new ResponseEntity<>(salary, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSalary(@Valid @RequestBody CreateSalaryDto salaryDto, @PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(salaryService.update(salaryMapper.fromCreateSalaryDtoToSalary(salaryDto), id),
                HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteSalary(@PathVariable Integer id) throws Exception {
        System.out.println(id);
        salaryService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
