package com.kti.restaurant.service.implementation;


import com.kti.restaurant.exception.ConflictException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Admin;
import com.kti.restaurant.model.Role;
import com.kti.restaurant.repository.AdminRepository;
import com.kti.restaurant.repository.RoleRepository;
import com.kti.restaurant.repository.UserRepository;
import com.kti.restaurant.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class AdminServiceUnitTests {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private AdminRepository adminRepository;

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
        Admin admin = new Admin("Vojnovic", "Andrija", "213123123", "andrija@vojnvo.com", "21312311");
        admin.setId(1);
        admin.setPassword(passwordEncoder.encode("andrija"));
        when(adminRepository.findById(1))
                .thenReturn(Optional.of(admin));
        when(userRepository.findByEmailAddress("andrija@vojnvo.com"))
                .thenReturn(admin);
    }

    @Test
    public void findById_ValidId_ExistingAdmin() throws Exception {
        Admin admin = adminService.findById(1);
        assertEquals(admin.getId(), 1);
        assertEquals(admin.getName(), "Andrija");
        assertEquals(admin.getLastName(), "Vojnovic");
        assertEquals(admin.getPhoneNumber(), "213123123");
        assertEquals(admin.getEmailAddress(), "andrija@vojnvo.com");
        assertEquals(admin.getAccountNumber(), "21312311");

    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException() throws Exception {
        assertThrows(MissingEntityException.class, () -> {
            adminService.findById(3);
        });
    }

    @Test
    public void create_ValidUniqueEmail_ValidAdmin() throws Exception {
        Admin adminForCreate = new Admin("Zolotic", "Milutin", "333333", "milutin@zolotic.com", "21312311");
        adminForCreate.setPassword("andrija");
        when(userRepository.findByEmailAddress("milutin@zolotic.com"))
                .thenReturn(null);
        when(adminRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));
        Role adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setName("ROLE_SYSTEM_ADMIN");
        when(roleRepository.getById(1L)).thenReturn(adminRole);
        Admin admin = this.adminService.create(adminForCreate);
        assertEquals(admin.getName(), "Milutin");
        assertEquals(admin.getLastName(), "Zolotic");
        assertEquals(admin.getPhoneNumber(), "333333");
        assertEquals(admin.getEmailAddress(), "milutin@zolotic.com");
        assertEquals(admin.getAccountNumber(), "21312311");
        assertEquals(admin.getRoles().get(0).getName(), "ROLE_SYSTEM_ADMIN");
        verify(adminRepository, times(1)).save(any());
    }

    @Test
    public void create_NonUnique_ThrowsConflictException() throws Exception {
        Admin adminForCreate = new Admin("Vojnovic", "Andrija", "333333", "andrija@vojnvo.com", "21312311");
        adminForCreate.setPassword("andrija");
        assertThrows(ConflictException.class, () -> {
            this.adminService.create(adminForCreate);
        });
        verify(adminRepository, times(0)).save(any());
    }

    @Test
    public void update_ValidAdmin_ValidAdmin() throws Exception {
        Admin adminForUpdate = new Admin("Vojnovic", "Andrija", "333333", "andrija@vojnvo.com", "21312311");
        adminForUpdate.setId(1);
        when(adminRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));
        Admin admin = this.adminService.update(adminForUpdate, 1);
        assertEquals(admin.getId(), 1);
        assertEquals(admin.getName(), "Andrija");
        assertEquals(admin.getLastName(), "Vojnovic");
        assertEquals(admin.getPhoneNumber(), "333333");
        assertEquals(admin.getEmailAddress(), "andrija@vojnvo.com");
        assertEquals(admin.getAccountNumber(), "21312311");
        verify(adminRepository, times(1)).findById(1);
        verify(adminRepository, times(1)).save(any());
    }

    @Test
    public void update_InvalidId_ThrowsMissingEntityException() throws Exception {
        assertThrows(MissingEntityException.class, () -> {
            this.adminService.update(null, 3);
        });
        verify(adminRepository, times(1)).findById(3);
        verify(adminRepository, times(0)).save(any());
    }

    @Test
    public void delete_ValidId_AdminDeleted() throws Exception {
        assertDoesNotThrow(() -> {
            this.adminService.delete(1);
        });
        verify(adminRepository, times(1)).findById(1);
        verify(adminRepository, times(1)).delete(any());
    }

    @Test
    public void delete_InvalidId_ThrowsMissingEntityException() throws Exception {
        assertThrows(MissingEntityException.class, () -> {
            this.adminService.delete(3);
        });
        verify(adminRepository, times(1)).findById(3);
        verify(adminRepository, times(0)).deleteById(3);
    }

}
