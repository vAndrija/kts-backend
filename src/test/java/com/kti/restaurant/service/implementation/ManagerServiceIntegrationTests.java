package com.kti.restaurant.service.implementation;


import com.kti.restaurant.exception.ConflictException;
import com.kti.restaurant.model.Manager;
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
public class ManagerServiceIntegrationTests {
    @Autowired
    private ManagerService managerService;

    @Test
    public void findAll_ReturnsExitingManagers() {
        List<Manager> managers = managerService.findAll();
        assertEquals(1, managers.size());
    }

    @Test
    public void findById_ValidId_ReturnsExistingManager() throws Exception {
        Manager manager = managerService.findById(6);
        assertEquals("sarajovic@gmail.com", manager.getEmailAddress());
        assertEquals("sara", manager.getName());
        assertEquals("jovic", manager.getLastName());
    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            managerService.findById(44);
        });
    }

    @Test
    public void create_ValidManager_ReturnsCreatedManager() throws Exception {
        Manager manager = new Manager("Vojnovic", "Andrija", "213123123", "andrija@vojnvo.com", "21312311");
        manager.setPassword("1234");
        Manager createdManager = managerService.create(manager);
        assertEquals("Vojnovic", createdManager.getLastName());
        assertEquals("Andrija", createdManager.getName());
        assertEquals("213123123", createdManager.getPhoneNumber());
        assertEquals("andrija@vojnvo.com", createdManager.getEmailAddress());
        assertEquals("21312311", createdManager.getAccountNumber());
    }

    @Test
    public void create_NonUnique_ThrowsConflictException() {
        Manager manager = new Manager("Vojnovic", "Andrija", "213123123", "sarajovic@gmail.com", "21312311");
        assertThrows(ConflictException.class, () -> {
            managerService.create(manager);
        });
    }

    @Rollback()
    @Test
    public void update_ValidId_ReturnsUpdatedManager() throws Exception {
        Manager manager = new Manager("Vojnovic", "Andrija", "213123123", "sarajovic@gmail.com", "21312311");
        Manager updatedManager = managerService.update(manager, 6);
        assertEquals("Vojnovic", updatedManager.getLastName());
        assertEquals("Andrija", updatedManager.getName());
        assertEquals("213123123", updatedManager.getPhoneNumber());
        assertEquals("sarajovic@gmail.com", updatedManager.getEmailAddress());
        assertEquals("21312311", updatedManager.getAccountNumber());

    }

    @Rollback()
    @Test
    void update_InvalidId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            managerService.update(null, 44);
        });
    }

    @Rollback()
    @Test
    void delete_ValidId() throws Exception {
        managerService.delete(6);
        Assertions.assertThrows(MissingEntityException.class, () -> {
            managerService.findById(6);
        });
    }

    @Rollback()
    @Test
    void delete_InvalidId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            managerService.delete(44);
        });
    }

}
