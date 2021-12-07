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

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class WaiterServiceIntegrationTests {

    @Autowired
    private WaiterService waiterService;

    @Test
    public void findAll_NumberOfWaiters(){
        List<Waiter> waiters = waiterService.findAll();
        assertEquals(waiters.size(),2);
    }

    @Test
    public void findById_ValidId_ExistingWaiter() throws Exception {
        Waiter waiter = waiterService.findById(7);
        assertEquals(waiter.getEmailAddress(), "jovanpetrovic@gmail.com");
    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException(){
        assertThrows(MissingEntityException.class, () -> {
            waiterService.findById(70);
        });
    }

    @Test
    public void create_UniqueEmail_ValidWaiter() throws Exception {
        Waiter waiterForCreate = new Waiter("Peric","Ana","065289632",
                "anaperic@gmail.com","412589632");
        waiterForCreate.setPassword("456");
        Waiter waiter = waiterService.create(waiterForCreate);
        assertEquals(waiter.getName(),"Ana");
        assertEquals(waiter.getLastName(), "Peric");
        assertEquals(waiter.getPhoneNumber(), "065289632");
        assertEquals(waiter.getAccountNumber(), "412589632");
        assertEquals(waiter.getRoles().get(0).getId(), 5L);

    }

    @Test
    public void create_NonUniqueEmail_ThrowsConflictException() throws Exception {
        Waiter waiterForCreate = new Waiter("Popovic", "Ana", "0654123699",
                "anapopovic@gmail.com", "02356987451");
        waiterForCreate.setPassword("123");
        assertThrows(ConflictException.class, ()->{
            waiterService.create(waiterForCreate);
        });

    }

    @Test
    public void update_ValidId_ExistingWaiter() throws Exception {
        Waiter waiterForUpdate =  new Waiter("Jovic", "Ana", "0632589411",
                "anapopovic@gmail.com", "0252698741");
        waiterForUpdate.setId(8);
        waiterForUpdate.setPassword("123");

        Waiter updatedWaiter = waiterService.update(waiterForUpdate,8);
        assertEquals(updatedWaiter.getName(), "Ana");
        assertEquals(updatedWaiter.getLastName(), "Jovic");
        assertEquals(updatedWaiter.getPhoneNumber(), "0632589411");
        assertEquals(updatedWaiter.getAccountNumber(), "0252698741");
    }

    @Test
    public void update_InvalidId_ThrowsMissingEntityException() throws Exception {
        assertThrows(MissingEntityException.class, ()->{
            waiterService.update(null,100);
        });
    }

    @Rollback
    @Test
    public void delete_ValidId_WaiterDeleted(){
        assertDoesNotThrow(() ->{
            waiterService.delete(8);
        });

    }

    @Rollback
    @Test
    public void delete_InvalidId_ThrowsMissingEntityException(){
        assertThrows(MissingEntityException.class, ()->{
            waiterService.delete(100);
        });
    }

}
