package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.ConflictException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Role;
import com.kti.restaurant.model.Waiter;
import com.kti.restaurant.repository.RoleRepository;
import com.kti.restaurant.repository.SalaryRepository;
import com.kti.restaurant.repository.UserRepository;
import com.kti.restaurant.repository.WaiterRepository;
import com.kti.restaurant.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class WaiterServiceUnitTests {

    @InjectMocks
    private WaiterService waiterService;

    @Mock
    private WaiterRepository waiterRepository;

    @Mock
    private SalaryRepository salaryRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        Waiter waiter = new Waiter("Milic", "Sara", "0654123699",
                "saramilic@gmail.com", "02356987451");
        waiter.setId(1);
        waiter.setPassword(passwordEncoder.encode("123"));

        when(waiterRepository.findById(1)).thenReturn(Optional.of(waiter));
        when(userRepository.findByEmailAddress("saramilic@gmail.com")).thenReturn(waiter);
    }

    @Test
    public void findById_ValidId_ReturnsExistingWaiter() throws Exception {
        Waiter waiter = waiterService.findById(1);

        assertEquals(1, waiter.getId());
        assertEquals("saramilic@gmail.com", waiter.getEmailAddress());
        assertEquals("Sara", waiter.getName());
        assertEquals("Milic", waiter.getLastName());
        assertEquals("0654123699", waiter.getPhoneNumber());
        assertEquals("02356987451", waiter.getAccountNumber());

    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            waiterService.findById(2);
        });
    }

    @Test
    public void create_UniqueEmail_ReturnsCreatedWaiter() throws Exception {
        Waiter waiterForCreate = new Waiter("Peric", "Ana", "065289632",
                "anaperic@gmail.com", "412589632");
        waiterForCreate.setId(2);
        waiterForCreate.setPassword("456");

        Role role = new Role();
        role.setId(5L);
        role.setName("ROLE_WAITER");
        when(roleRepository.getById(5L)).thenReturn(role);

        List<Role> roles = new ArrayList<Role>();
        roles.add(role);
        waiterForCreate.setRoles(roles);
        when(salaryRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));

        when(userRepository.findByEmailAddress("anaperic@gmail.com")).thenReturn(null);
        when(waiterRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));

        Waiter waiter = waiterService.create(waiterForCreate);
        assertEquals(2, waiter.getId());
        assertEquals("Ana", waiter.getName());
        assertEquals("Peric", waiter.getLastName());
        assertEquals("065289632", waiter.getPhoneNumber());
        assertEquals("412589632", waiter.getAccountNumber());
        assertEquals(5L, waiter.getRoles().get(0).getId());

    }

    @Test
    public void create_NonUniqueEmail_ThrowsConflictException() throws Exception {
        Waiter waiterForCreate = new Waiter("Milic", "Sara", "0654123699",
                "saramilic@gmail.com", "02356987451");
        waiterForCreate.setId(1);
        waiterForCreate.setPassword("123");

        assertThrows(ConflictException.class, () -> {
            waiterService.create(waiterForCreate);
        });

    }

    @Test
    public void update_ValidId_ReturnsUpdatedWaiter() throws Exception {
        Waiter waiterForUpdate = new Waiter("Jovic", "Tara", "0632589411",
                "saramilic@gmail.com", "0252698741");
        waiterForUpdate.setId(1);
        waiterForUpdate.setPassword(passwordEncoder.encode("123"));

        when(waiterRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));

        Waiter updatedWaiter = waiterService.update(waiterForUpdate, 1);
        assertEquals("Tara", updatedWaiter.getName());
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
    public void delete_ValidId() {
        assertDoesNotThrow(() -> {
            waiterService.delete(1);
        });

        verify(waiterRepository, times(1)).findById(1);
        verify(waiterRepository, times(1)).delete(any(Waiter.class));
    }

    @Test
    public void delete_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            waiterService.delete(100);
        });

        verify(waiterRepository, times(1)).findById(100);
        verify(waiterRepository, times(0)).delete(any(Waiter.class));

    }
}
