package com.kti.restaurant.service.implementation;


import com.kti.restaurant.exception.ConflictException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Admin;
import com.kti.restaurant.model.Role;
import com.kti.restaurant.repository.AdminRepository;
import com.kti.restaurant.repository.RoleRepository;
import com.kti.restaurant.repository.SalaryRepository;
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
public class AdminServiceUnitTests {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private SalaryRepository salaryRepository;

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
        when(adminRepository.findById(1))
                .thenReturn(Optional.of(admin));
        when(userRepository.findByEmailAddress("andrija@vojnvo.com"))
                .thenReturn(admin);
    }

    @Test
    public void findById_ValidId_ReturnsExistingAdmin() throws Exception {
        Admin admin = adminService.findById(1);
        assertEquals( 1,admin.getId());
        assertEquals( "Andrija",admin.getName());
        assertEquals( "Vojnovic",admin.getLastName());
        assertEquals("213123123",admin.getPhoneNumber());
        assertEquals( "andrija@vojnvo.com",admin.getEmailAddress());
        assertEquals( "21312311",admin.getAccountNumber());
    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException() throws Exception {
        assertThrows(MissingEntityException.class, () -> {
            adminService.findById(3);
        });
    }

    @Test
    public void create_ValidUniqueEmailValidAdmin_ReturnsCreatedAdmin() throws Exception {
        Admin adminForCreate = new Admin("Zolotic", "Milutin", "333333", "milutin@zolotic.com", "21312311");
        adminForCreate.setPassword("andrija");
        when(userRepository.findByEmailAddress("milutin@zolotic.com"))
                .thenReturn(null);
        when(adminRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));
        when(salaryRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));
        Role adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setName("ROLE_SYSTEM_ADMIN");
        when(roleRepository.getById(1L)).thenReturn(adminRole);
        Admin admin = this.adminService.create(adminForCreate);
        assertEquals("Milutin",admin.getName() );
        assertEquals( "Zolotic",admin.getLastName());
        assertEquals( "333333",admin.getPhoneNumber());
        assertEquals( "milutin@zolotic.com",admin.getEmailAddress());
        assertEquals("21312311",admin.getAccountNumber());
        assertEquals("ROLE_SYSTEM_ADMIN",admin.getRoles().get(0).getName());
        verify(adminRepository, times(1)).save(any());
    }

    @Test
    public void create_NonUnique_ThrowsConflictException() {
        Admin adminForCreate = new Admin("Vojnovic", "Andrija", "333333", "andrija@vojnvo.com", "21312311");
        adminForCreate.setPassword("andrija");
        assertThrows(ConflictException.class, () -> {
            this.adminService.create(adminForCreate);
        });
        verify(adminRepository, times(0)).save(any());
    }

    @Test
    public void update_ValidAdmin_ReturnsUpdatedAdmin() throws Exception {
        Admin adminForUpdate = new Admin("Vojnovic", "Andrija", "333333", "andrija@vojnvo.com", "21312311");
        adminForUpdate.setId(1);
        when(adminRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));
        Admin admin = this.adminService.update(adminForUpdate, 1);
        assertEquals(admin.getId(), 1);
        assertEquals( "Andrija",admin.getName());
        assertEquals( "Vojnovic",admin.getLastName());
        assertEquals("333333",admin.getPhoneNumber());
        assertEquals( "andrija@vojnvo.com",admin.getEmailAddress());
        assertEquals( "21312311",admin.getAccountNumber());
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
    public void delete_ValidId() {
        assertDoesNotThrow(() -> {
            this.adminService.delete(1);
        });
        verify(adminRepository, times(1)).findById(1);
        verify(adminRepository, times(1)).delete(any());
    }

    @Test
    public void delete_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            this.adminService.delete(3);
        });
        verify(adminRepository, times(1)).findById(3);
        verify(adminRepository, times(0)).deleteById(3);
    }

}
