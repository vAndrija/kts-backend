package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.ConflictException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Cook;
import com.kti.restaurant.model.Role;
import com.kti.restaurant.repository.CookRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class CookServiceUnitTests {
    @InjectMocks
    private CookService cookService;

    @Mock
    private CookRepository cookRepository;

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
        Cook cook = new Cook("Vojnovic", "Andrija", "213123123", "andrija@vojnvo.com", "21312311", true);
        cook.setId(1);
        when(cookRepository.findById(1))
                .thenReturn(Optional.of(cook));
        when(userRepository.findByEmailAddress("andrija@vojnvo.com"))
                .thenReturn(cook);
    }

    @Test
    public void findById_ValidId_ReturnsExistingCook() throws Exception {
        Cook cook = cookService.findById(1);
        assertEquals(1, cook.getId());
        assertEquals("Andrija", cook.getName());
        assertEquals("Vojnovic", cook.getLastName());
        assertEquals("213123123", cook.getPhoneNumber());
        assertEquals("andrija@vojnvo.com", cook.getEmailAddress());
        assertEquals("21312311", cook.getAccountNumber());
        assertEquals(true, cook.getPriority());

    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException() throws Exception {
        assertThrows(MissingEntityException.class, () -> {
            cookService.findById(3);
        });
    }

    @Test
    public void create_ValidUniqueEmail_ReturnsCreatedCook() throws Exception {
        Cook cookForCreate = new Cook("Zolotic", "Milutin", "333333", "milutin@zolotic.com", "21312311", true);
        cookForCreate.setPassword("andrija");
        when(userRepository.findByEmailAddress("milutin@zolotic.com"))
                .thenReturn(null);
        when(cookRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));
        Role CookRole = new Role();
        CookRole.setId(3L);
        CookRole.setName("ROLE_SYSTEM_COOK");
        when(roleRepository.getById(3L)).thenReturn(CookRole);
        Cook cook = this.cookService.create(cookForCreate);
        assertEquals("Milutin", cook.getName());
        assertEquals("Zolotic", cook.getLastName());
        assertEquals("333333", cook.getPhoneNumber());
        assertEquals("milutin@zolotic.com", cook.getEmailAddress());
        assertEquals("21312311", cook.getAccountNumber());
        assertEquals("ROLE_SYSTEM_COOK", cook.getRoles().get(0).getName());
        assertEquals(true, cook.getPriority());
        verify(cookRepository, times(1)).save(any());
    }

    @Test
    public void create_NonUnique_ThrowsConflictException() {
        Cook CookForCreate = new Cook("Zolotic", "Milutin", "333333", "andrija@vojnvo.com", "21312311", true);
        CookForCreate.setPassword("andrija");
        assertThrows(ConflictException.class, () -> {
            this.cookService.create(CookForCreate);
        });
        verify(cookRepository, times(0)).save(any());
    }

    @Test
    public void update_ValidCook_ReturnsUpdatedCook() throws Exception {
        Cook CookForUpdate = new Cook("Vojnovic", "Andrija", "21312312323", "andrija@vojnvo.com", "21312311", false);
        CookForUpdate.setId(1);
        when(cookRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));
        Cook Cook = this.cookService.update(CookForUpdate, 1);
        assertEquals(Cook.getId(), 1);
        assertEquals("Andrija", Cook.getName());
        assertEquals("Vojnovic", Cook.getLastName());
        assertEquals("21312312323", Cook.getPhoneNumber());
        assertEquals("andrija@vojnvo.com", Cook.getEmailAddress());
        assertEquals("21312311", Cook.getAccountNumber());
        assertEquals(false, Cook.getPriority());
        verify(cookRepository, times(1)).findById(1);
        verify(cookRepository, times(1)).save(any());
    }

    @Test
    public void update_InvalidId_ThrowsMissingEntityException() throws Exception {
        assertThrows(MissingEntityException.class, () -> {
            this.cookService.update(null, 3);
        });
        verify(cookRepository, times(1)).findById(3);
        verify(cookRepository, times(0)).save(any());
    }

    @Test
    public void delete_ValidId() {
        assertDoesNotThrow(() -> {
            this.cookService.delete(1);
        });
        verify(cookRepository, times(1)).findById(1);
        verify(cookRepository, times(1)).delete(any());
    }

    @Test
    public void delete_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            this.cookService.delete(3);
        });
        verify(cookRepository, times(1)).findById(3);
        verify(cookRepository, times(0)).deleteById(3);
    }
}
