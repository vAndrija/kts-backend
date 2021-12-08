package com.kti.restaurant.service.implementation;


import com.kti.restaurant.model.Admin;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.model.enums.MenuItemType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;



@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class AdminServiceIntegrationTests {
    @Autowired
    private AdminService adminService;

    @Test
    public void  findAll_ValidNumberOfAdmins() {
        List<Admin> admins  =  adminService.findAll();
        assertEquals(5,admins.size());
    }
}
