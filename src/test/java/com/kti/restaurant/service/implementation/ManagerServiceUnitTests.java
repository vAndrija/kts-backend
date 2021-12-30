package com.kti.restaurant.service.implementation;


import com.kti.restaurant.exception.ConflictException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Manager;
import com.kti.restaurant.model.Role;
import com.kti.restaurant.repository.ManagerRepository;
import com.kti.restaurant.repository.ManagerRepository;
import com.kti.restaurant.repository.RoleRepository;
import com.kti.restaurant.repository.UserRepository;
import com.kti.restaurant.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class ManagerServiceUnitTests {

    @InjectMocks
    private ManagerService managerService;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        Manager manager = new Manager("Vojnovic", "Andrija", "213123123", "andrija@vojnvo.com", "21312311");
        manager.setId(1);
        when(managerRepository.findById(1))
                .thenReturn(Optional.of(manager));
        when(userRepository.findByEmailAddress("andrija@vojnvo.com"))
                .thenReturn(manager);
    }

    @Test
    public void findById_ValidId_ExistingManager() throws Exception {
        Manager manager = managerService.findById(1);
        assertEquals(manager.getId(), 1);
        assertEquals(manager.getName(), "Andrija");
        assertEquals(manager.getLastName(), "Vojnovic");
        assertEquals(manager.getPhoneNumber(), "213123123");
        assertEquals(manager.getEmailAddress(), "andrija@vojnvo.com");
        assertEquals(manager.getAccountNumber(), "21312311");

    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException() throws Exception {
        assertThrows(MissingEntityException.class, () -> {
            managerService.findById(3);
        });
    }

    @Test
    public void create_ValidUniqueEmail_ReturnsCreatedManager() throws Exception {
        Manager managerForCreate = new Manager("Zolotic", "Milutin", "333333", "milutin@zolotic.com", "21312311");
        managerForCreate.setPassword("andrija");
        when(userRepository.findByEmailAddress("milutin@zolotic.com"))
                .thenReturn(null);
        when(managerRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));
        Role managerRole = new Role();
        managerRole.setId(4L);
        managerRole.setName("ROLE_SYSTEM_MANAGER");
        when(roleRepository.getById(4L)).thenReturn(managerRole);
        Manager manager = this.managerService.create(managerForCreate);
        assertEquals(manager.getName(), "Milutin");
        assertEquals(manager.getLastName(), "Zolotic");
        assertEquals(manager.getPhoneNumber(), "333333");
        assertEquals(manager.getEmailAddress(), "milutin@zolotic.com");
        assertEquals(manager.getAccountNumber(), "21312311");
        assertEquals(manager.getRoles().get(0).getName(), "ROLE_SYSTEM_MANAGER");
        verify(managerRepository, times(1)).save(any());
    }

    @Test
    public void create_NonUnique_ThrowsConflictException() {
        Manager managerForCreate = new Manager("Vojnovic", "Andrija", "333333", "andrija@vojnvo.com", "21312311");
        managerForCreate.setPassword("andrija");
        assertThrows(ConflictException.class, () -> {
            this.managerService.create(managerForCreate);
        });
        verify(managerRepository, times(0)).save(any());
    }

    @Test
    public void update_ValidManager_ReturnsUpdatedManager() throws Exception {
        Manager managerForUpdate = new Manager("Vojnovic", "Andrija", "333333", "andrija@vojnvo.com", "21312311");
        managerForUpdate.setId(1);
        when(managerRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));
        Manager manager = this.managerService.update(managerForUpdate, 1);
        assertEquals(manager.getId(), 1);
        assertEquals(manager.getName(), "Andrija");
        assertEquals(manager.getLastName(), "Vojnovic");
        assertEquals(manager.getPhoneNumber(), "333333");
        assertEquals(manager.getEmailAddress(), "andrija@vojnvo.com");
        assertEquals(manager.getAccountNumber(), "21312311");
        verify(managerRepository, times(1)).findById(1);
        verify(managerRepository, times(1)).save(any());
    }

    @Test
    public void update_InvalidId_ThrowsMissingEntityException() throws Exception {
        assertThrows(MissingEntityException.class, () -> {
            this.managerService.update(null, 3);
        });
        verify(managerRepository, times(1)).findById(3);
        verify(managerRepository, times(0)).save(any());
    }

    @Test
    public void delete_ValidId() {
        assertDoesNotThrow(() -> {
            this.managerService.delete(1);
        });
        verify(managerRepository, times(1)).findById(1);
        verify(managerRepository, times(1)).delete(any());
    }

    @Test
    public void delete_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            this.managerService.delete(3);
        });
        verify(managerRepository, times(1)).findById(3);
        verify(managerRepository, times(0)).deleteById(3);
    }

}
