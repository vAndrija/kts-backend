package com.kti.restaurant.controller;

import com.kti.restaurant.dto.admin.AdminCreateDto;
import com.kti.restaurant.dto.admin.AdminDto;
import com.kti.restaurant.mapper.AdminMapper;
import com.kti.restaurant.model.Admin;
import com.kti.restaurant.service.contract.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping(value="/api/v1/admin")
public class AdminController {

    private IAdminService adminService;
    private AdminMapper adminMapper;

    @Autowired
    public AdminController(IAdminService adminService, AdminMapper adminMapper){
        this.adminService = adminService;
        this.adminMapper = adminMapper;
    }

    @PostMapping("")
    public ResponseEntity<?> createAdmin(@RequestBody AdminCreateDto adminCreateDto){
        Admin admin =  adminService.create(adminMapper.fromAdminCreateDtoToAdmin(adminCreateDto));
        return new ResponseEntity<>(adminMapper.fromAdminToAdminDto(admin), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<?> getAdmins(){
        List<AdminDto> adminsDto = adminService.findAll().stream()
                .map(admin->this.adminMapper.fromAdminToAdminDto(admin)).collect(Collectors.toList());
        return new ResponseEntity<>(adminsDto,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdminByEmail(@PathVariable Integer id){

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Integer id){

    }

    @DeleteMapping("/{id")
    public ResponseEntity<?> deleteAdmin(@PathVariable Integer id){

    }
}
