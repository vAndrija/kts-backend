package com.kti.restaurant.service.implementation;


import com.kti.restaurant.exception.ConflictException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Bartender;
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
public class BartenderServiceIntegrationTests {

    @Autowired
    private BartenderService bartenderService;

    @Test
    public void findAll_ReturnsExistingBartenders() {
        List<Bartender> bartenders = bartenderService.findAll();
        assertEquals(2, bartenders.size());
    }

    @Test
    public void findById_ValidId_ReturnsExistingBartender() throws Exception {
        Bartender bartender = bartenderService.findById(3);
        assertEquals("milossaric@gmail.com", bartender.getEmailAddress());
        assertEquals("milos", bartender.getName());
        assertEquals("saric", bartender.getLastName());
    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            bartenderService.findById(44);
        });
    }

    @Test
    public void create_ValidBartender_ReturnsCreatedBartender() throws Exception {
        Bartender bartender = new Bartender("Vojnovic", "Andrija", "213123123", "andrija@vojnvo.com", "21312311", true);
        bartender.setPassword("1234");
        Bartender createdBartender = bartenderService.create(bartender);
        assertEquals("Vojnovic", createdBartender.getLastName());
        assertEquals("Andrija", createdBartender.getName());
        assertEquals("213123123", createdBartender.getPhoneNumber());
        assertEquals("andrija@vojnvo.com", createdBartender.getEmailAddress());
        assertEquals("21312311", createdBartender.getAccountNumber());
        assertEquals(true, createdBartender.getPriority());
    }

    @Test
    public void create_NonUnique_ThrowsConflictException() {
        Bartender bartender = new Bartender("Vojnovic", "Andrija", "213123123", "milossaric@gmail.com", "21312311", true);
        assertThrows(ConflictException.class, () -> {
            bartenderService.create(bartender);
        });
    }

    @Rollback()
    @Test
    public void update_ValidId_ReturnsUpdatedBartender() throws Exception {
        Bartender bartender = new Bartender("Vojnovic", "Andrija", "213123123", "milossaric@gmail.com", "21312311", true);
        Bartender updatedBartender = bartenderService.update(bartender, 3);
        assertEquals("Vojnovic", updatedBartender.getLastName());
        assertEquals("Andrija", updatedBartender.getName());
        assertEquals("213123123", updatedBartender.getPhoneNumber());
        assertEquals("milossaric@gmail.com", updatedBartender.getEmailAddress());
        assertEquals("21312311", updatedBartender.getAccountNumber());

    }

    @Rollback()
    @Test
    void update_InvalidId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            bartenderService.update(null, 44);
        });
    }

    @Rollback()
    @Test
    void delete_ValidId() throws Exception {
        bartenderService.delete(3);
        Assertions.assertThrows(MissingEntityException.class, () -> {
            bartenderService.findById(3);
        });
    }

    @Rollback()
    @Test
    void delete_InvalidId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            bartenderService.delete(44);
        });
    }
}
