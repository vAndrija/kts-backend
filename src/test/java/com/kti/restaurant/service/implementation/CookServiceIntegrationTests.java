package com.kti.restaurant.service.implementation;


import com.kti.restaurant.exception.ConflictException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Cook;
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
public class CookServiceIntegrationTests {

    @Autowired
    private CookService cookService;

    @Test
    public void findAll_ValidNumberOfCooks() {
        List<Cook> cooks = cookService.findAll();
        assertEquals(2, cooks.size());
    }

    @Test
    public void findById_ValidId_ValidCook() throws Exception {
        Cook cook = cookService.findById(4);
        assertEquals("kristinamisic@gmail.com", cook.getEmailAddress());
        assertEquals("kristina", cook.getName());
        assertEquals("misic", cook.getLastName());
    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            cookService.findById(44);
        });
    }

    @Test
    public void create_ValidCook() throws Exception {
        Cook cook = new Cook("Vojnovic", "Andrija", "213123123", "andrija@vojnvo.com", "21312311", true);
        cook.setPassword("1234");
        Cook createdCook = cookService.create(cook);
        assertEquals("Vojnovic", createdCook.getLastName());
        assertEquals("Andrija", createdCook.getName());
        assertEquals("213123123", createdCook.getPhoneNumber());
        assertEquals("andrija@vojnvo.com", createdCook.getEmailAddress());
        assertEquals("21312311", createdCook.getAccountNumber());
        assertEquals(true, createdCook.getPriority());
    }

    @Test
    public void create_NonUnique_ThrowsConflictException() {
        Cook cook = new Cook("Vojnovic", "Andrija", "213123123", "milossaric@gmail.com", "21312311", true);
        assertThrows(ConflictException.class, () -> {
            cookService.create(cook);
        });
    }

    @Rollback()
    @Test
    public void update_ValidId_ValidCook() throws Exception {
        Cook cook = new Cook("Vojnovic", "Andrija", "213123123", "kristinamisic@gmail.com", "21312311", true);
        Cook updatedCook = cookService.update(cook, 4);
        assertEquals("Vojnovic", updatedCook.getLastName());
        assertEquals("Andrija", updatedCook.getName());
        assertEquals("213123123", updatedCook.getPhoneNumber());
        assertEquals("kristinamisic@gmail.com", updatedCook.getEmailAddress());
        assertEquals("21312311", updatedCook.getAccountNumber());

    }

    @Rollback()
    @Test
    void update_InvalidId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            cookService.update(null, 44);
        });
    }

    @Rollback()
    @Test
    void delete_ValidId_ThrowsMissingEntityException() throws Exception {
        cookService.delete(4);
        Assertions.assertThrows(MissingEntityException.class, () -> {
            cookService.findById(4);
        });
    }

    @Rollback()
    @Test
    void delete_InvalidId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            cookService.delete(44);
        });
    }
}
