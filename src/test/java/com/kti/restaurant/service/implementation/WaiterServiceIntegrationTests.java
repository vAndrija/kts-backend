package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.ConflictException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Waiter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class WaiterServiceIntegrationTests {

    @Autowired
    private WaiterService waiterService;

    @Test
    public void findAll_NumberOfWaiters() {
        List<Waiter> waiters = waiterService.findAll();

        assertEquals(2, waiters.size());
    }

    @Test
    public void findById_ValidId_ExistingWaiter() throws Exception {
        Waiter waiter = waiterService.findById(7);

        assertEquals("jovanpetrovic@gmail.com", waiter.getEmailAddress());
        assertEquals("jovan", waiter.getName());
        assertEquals("petrovic", waiter.getLastName());
        assertEquals("0607425922", waiter.getPhoneNumber());
        assertEquals("22365612316263", waiter.getAccountNumber());
        assertEquals(5L, waiter.getRoles().get(0).getId());
    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            waiterService.findById(70);
        });
    }

    @Test
    @Rollback
    public void create_UniqueEmail_ValidWaiter() throws Exception {
        Waiter waiterForCreate = new Waiter("Peric", "Ana", "065289632",
                "anaperic@gmail.com", "412589632");
        waiterForCreate.setPassword("456");
        Waiter waiter = waiterService.create(waiterForCreate);

        assertEquals("Ana", waiter.getName());
        assertEquals("Peric", waiter.getLastName());
        assertEquals("065289632", waiter.getPhoneNumber());
        assertEquals("412589632", waiter.getAccountNumber());
        assertEquals(5L, waiter.getRoles().get(0).getId());

    }

    @Test
    public void create_NonUniqueEmail_ThrowsConflictException() throws Exception {
        Waiter waiterForCreate = new Waiter("Popovic", "Ana", "0654123699",
                "anapopovic@gmail.com", "02356987451");
        waiterForCreate.setPassword("123");

        assertThrows(ConflictException.class, () -> {
            waiterService.create(waiterForCreate);
        });

    }

    @Test
    @Rollback
    public void update_ValidId_ExistingWaiter() throws Exception {
        Waiter waiterForUpdate = new Waiter("Jovic", "Ana", "0632589411",
                "anapopovic@gmail.com", "0252698741");
        waiterForUpdate.setId(8);
        waiterForUpdate.setPassword("123");

        Waiter updatedWaiter = waiterService.update(waiterForUpdate, 8);

        assertEquals("Ana", updatedWaiter.getName());
        assertEquals("Jovic", updatedWaiter.getLastName());
        assertEquals("0632589411", updatedWaiter.getPhoneNumber());
        assertEquals("0252698741", updatedWaiter.getAccountNumber());
    }

    @Test
    public void update_InvalidId_ThrowsMissingEntityException() throws Exception {
        assertThrows(MissingEntityException.class, () -> {
            waiterService.update(null, 100);
        });
    }


    @Test
    @Rollback
    public void delete_ValidId_WaiterDeleted() throws Exception {
        waiterService.delete(8);
        assertThrows(MissingEntityException.class, () -> {
            waiterService.findById(8);
        });

    }

    @Test
    public void delete_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            waiterService.delete(100);
        });
    }

}
