package com.kti.restaurant.service.implementation;


import com.kti.restaurant.exception.ConflictException;
import com.kti.restaurant.model.Admin;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.model.enums.MenuItemType;
import org.hibernate.annotations.UpdateTimestamp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class AdminServiceIntegrationTests {
    @Autowired
    private AdminService adminService;

    @Test
    public void findAll_ValidNumberOfAdmins() {
        List<Admin> admins = adminService.findAll();
        assertEquals(5, admins.size());
    }

    @Test
    public void findById_ValidId_ValidAdmin() throws Exception {
        Admin admin = adminService.findById(1);
        assertEquals("mirkomiric@gmail.com", admin.getEmailAddress());
        assertEquals("mirko", admin.getName());
        assertEquals("miric", admin.getLastName());
    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            adminService.findById(44);
        });
    }

    @Test
    public void create_ValidAdmin() throws Exception {
        Admin admin = new Admin("Vojnovic", "Andrija", "213123123", "andrija@vojnvo.com", "21312311");
        admin.setPassword("1234");
        Admin createdAdmin = adminService.create(admin);
        assertEquals("Vojnovic", createdAdmin.getLastName());
        assertEquals("Andrija", createdAdmin.getName());
        assertEquals("213123123", createdAdmin.getPhoneNumber());
        assertEquals("andrija@vojnvo.com", createdAdmin.getEmailAddress());
        assertEquals("21312311", createdAdmin.getAccountNumber());
    }

    @Test
    public void create_NonUnique_ThrowsConflictException() {
        Admin admin = new Admin("Vojnovic", "Andrija", "213123123", "mirkomiric@gmail.com", "21312311");
        assertThrows(ConflictException.class, () -> {
            adminService.create(admin);
        });
    }

    @Rollback()
    @Test
    public void update_ValidId_ValidAdmin() throws Exception {
        Admin admin = new Admin("Vojnovic", "Andrija", "213123123", "mirkomiric@gmail.com", "21312311");
        Admin updatedAdmin = adminService.update(admin, 1);
        assertEquals("Vojnovic", updatedAdmin.getLastName());
        assertEquals("Andrija", updatedAdmin.getName());
        assertEquals("213123123", updatedAdmin.getPhoneNumber());
        assertEquals("mirkomiric@gmail.com", updatedAdmin.getEmailAddress());
        assertEquals("21312311", updatedAdmin.getAccountNumber());

    }

    @Rollback()
    @Test
    void update_InvalidId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            adminService.update(null, 44);
        });
    }

    @Rollback()
    @Test
    void delete_ValidId_ThrowsMissingEntityException() throws Exception {
        adminService.delete(1);
        Assertions.assertThrows(MissingEntityException.class, () -> {
            adminService.findById(1);
        });
    }

    @Rollback()
    @Test
    void delete_InvalidId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            adminService.delete(44);
        });
    }

}
