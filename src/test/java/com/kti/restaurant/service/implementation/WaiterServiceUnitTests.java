package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.ConflictException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Role;
import com.kti.restaurant.model.Waiter;
import com.kti.restaurant.repository.RoleRepository;
import com.kti.restaurant.repository.UserRepository;
import com.kti.restaurant.repository.WaiterRepository;
import com.kti.restaurant.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class WaiterServiceUnitTests {

    @InjectMocks
    private WaiterService waiterService;

    @Mock
    private WaiterRepository waiterRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup(){
        Waiter waiter = new Waiter("Milic", "Sara", "0654123699",
                "saramilic@gmail.com", "02356987451");
        waiter.setId(1);
        waiter.setPassword(passwordEncoder.encode("123"));

        when(waiterRepository.findById(1)).thenReturn(Optional.of(waiter));
        when(userRepository.findByEmailAddress("saramilic@gmail.com")).thenReturn(waiter);
    }

    @Test
    public void findById_ValidId_ExistingWaiter() throws Exception {
        Waiter waiter = waiterService.findById(1);

        assertEquals(waiter.getId(), 1);
        assertEquals(waiter.getEmailAddress(), "saramilic@gmail.com");
        assertEquals(waiter.getName(), "Sara");
        assertEquals(waiter.getLastName(), "Milic");
        assertEquals(waiter.getPhoneNumber(), "0654123699");
        assertEquals(waiter.getAccountNumber(), "02356987451");

    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException(){
        assertThrows(MissingEntityException.class, ()->{
           waiterService.findById(2);
        });
    }

    @Test
    public void create_UniqueEmail_ValidWaiter() throws Exception {
        Waiter waiterForCreate = new Waiter("Peric","Ana","065289632",
                "anaperic@gmail.com","412589632");
        waiterForCreate.setId(2);
        waiterForCreate.setPassword("456");

        Role role = new Role();
        role.setId(5L);
        role.setName("ROLE_WAITER");
        when(roleRepository.getById(5L)).thenReturn(role);

        List<Role> roles = new ArrayList<Role>();
        roles.add(role);
        waiterForCreate.setRoles(roles);

        when(userRepository.findByEmailAddress("anaperic@gmail.com")).thenReturn(null);
        when(waiterRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));

        Waiter waiter = waiterService.create(waiterForCreate);
        assertEquals(waiter.getId(),2);
        assertEquals(waiter.getName(),"Ana");
        assertEquals(waiter.getLastName(), "Peric");
        assertEquals(waiter.getPhoneNumber(), "065289632");
        assertEquals(waiter.getAccountNumber(), "412589632");
        assertEquals(waiter.getRoles().get(0).getId(), 5L);

    }

    @Test
    public void create_NonUniqueEmail_ThrowsConflictException() throws Exception {
        Waiter waiterForCreate = new Waiter("Milic", "Sara", "0654123699",
                "saramilic@gmail.com", "02356987451");
        waiterForCreate.setId(1);
        waiterForCreate.setPassword("123");
        assertThrows(ConflictException.class, ()->{
           waiterService.create(waiterForCreate);
        });

    }

    @Test
    public void update_ValidId_ExistingWaiter() throws Exception {
        Waiter waiterForUpdate =  new Waiter("Jovic", "Tara", "0632589411",
                "saramilic@gmail.com", "0252698741");
        waiterForUpdate.setId(1);
        waiterForUpdate.setPassword(passwordEncoder.encode("123"));

        when(waiterRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));

        Waiter updatedWaiter = waiterService.update(waiterForUpdate,1);
        assertEquals(updatedWaiter.getName(), "Tara");
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

    @Test
    public void delete_ValidId_WaiterDeleted(){
        assertDoesNotThrow(() ->{
            waiterService.delete(1);
        });
        verify(waiterRepository, times(1)).findById(1);
        verify(waiterRepository, times(1)).delete(any(Waiter.class));
    }

    @Test
    public void delete_InvalidId_ThrowsMissingEntityException(){
        assertThrows(MissingEntityException.class, ()->{
            waiterService.delete(100);
        });

    }
}
