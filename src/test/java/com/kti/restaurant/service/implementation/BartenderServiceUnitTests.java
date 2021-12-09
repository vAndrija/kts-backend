package com.kti.restaurant.service.implementation;


import com.kti.restaurant.exception.ConflictException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Admin;
import com.kti.restaurant.model.Bartender;
import com.kti.restaurant.model.Role;
import com.kti.restaurant.repository.AdminRepository;
import com.kti.restaurant.repository.BartenderRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class BartenderServiceUnitTests {

    @InjectMocks
    private BartenderService bartenderService;

    @Mock
    private BartenderRepository bartenderRepository;

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
        Bartender bartender = new Bartender("Vojnovic", "Andrija", "213123123", "andrija@vojnvo.com", "21312311", true);
        bartender.setId(1);
        when(bartenderRepository.findById(1))
                .thenReturn(Optional.of(bartender));
        when(userRepository.findByEmailAddress("andrija@vojnvo.com"))
                .thenReturn(bartender);
    }

    @Test
    public void findById_ValidId_ExistingBartender() throws Exception {
        Bartender bartender = bartenderService.findById(1);
        assertEquals(1, bartender.getId());
        assertEquals("Andrija", bartender.getName());
        assertEquals("Vojnovic", bartender.getLastName());
        assertEquals("213123123", bartender.getPhoneNumber());
        assertEquals("andrija@vojnvo.com", bartender.getEmailAddress());
        assertEquals("21312311", bartender.getAccountNumber());
        assertEquals(true, bartender.getPriority());

    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException() throws Exception {
        assertThrows(MissingEntityException.class, () -> {
            bartenderService.findById(3);
        });
    }

    @Test
    public void create_ValidUniqueEmail_ValidBartender() throws Exception {
        Bartender bartenderForCreate = new Bartender("Zolotic", "Milutin", "333333", "milutin@zolotic.com", "21312311", true);
        bartenderForCreate.setPassword("andrija");
        when(userRepository.findByEmailAddress("milutin@zolotic.com"))
                .thenReturn(null);
        when(bartenderRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));
        Role bartenderRole = new Role();
        bartenderRole.setId(2L);
        bartenderRole.setName("ROLE_SYSTEM_BARTENDER");
        when(roleRepository.getById(2L)).thenReturn(bartenderRole);
        Bartender bartender = this.bartenderService.create(bartenderForCreate);
        assertEquals("Milutin", bartender.getName());
        assertEquals("Zolotic", bartender.getLastName());
        assertEquals("333333", bartender.getPhoneNumber());
        assertEquals("milutin@zolotic.com", bartender.getEmailAddress());
        assertEquals("21312311", bartender.getAccountNumber());
        assertEquals("ROLE_SYSTEM_BARTENDER", bartender.getRoles().get(0).getName());
        assertEquals(true, bartender.getPriority());
        verify(bartenderRepository, times(1)).save(any());
    }

    @Test
    public void create_NonUnique_ThrowsConflictException() {
        Bartender bartenderForCreate = new Bartender("Zolotic", "Milutin", "333333", "andrija@vojnvo.com", "21312311", true);
        bartenderForCreate.setPassword("andrija");
        assertThrows(ConflictException.class, () -> {
            this.bartenderService.create(bartenderForCreate);
        });
        verify(bartenderRepository, times(0)).save(any());
    }

    @Test
    public void update_ValidBartender_ValidBartender() throws Exception {
        Bartender bartenderForUpdate = new Bartender("Vojnovic", "Andrija", "21312312323", "andrija@vojnvo.com", "21312311", false);
        bartenderForUpdate.setId(1);
        when(bartenderRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));
        Bartender bartender = this.bartenderService.update(bartenderForUpdate, 1);
        assertEquals(bartender.getId(), 1);
        assertEquals("Andrija", bartender.getName());
        assertEquals("Vojnovic", bartender.getLastName());
        assertEquals("21312312323", bartender.getPhoneNumber());
        assertEquals("andrija@vojnvo.com", bartender.getEmailAddress());
        assertEquals("21312311", bartender.getAccountNumber());
        assertEquals(false, bartender.getPriority());
        verify(bartenderRepository, times(1)).findById(1);
        verify(bartenderRepository, times(1)).save(any());
    }

    @Test
    public void update_InvalidId_ThrowsMissingEntityException() throws Exception {
        assertThrows(MissingEntityException.class, () -> {
            this.bartenderService.update(null, 3);
        });
        verify(bartenderRepository, times(1)).findById(3);
        verify(bartenderRepository, times(0)).save(any());
    }

    @Test
    public void delete_ValidId_BartenderDeleted() {
        assertDoesNotThrow(() -> {
            this.bartenderService.delete(1);
        });
        verify(bartenderRepository, times(1)).findById(1);
        verify(bartenderRepository, times(1)).delete(any());
    }

    @Test
    public void delete_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            this.bartenderService.delete(3);
        });
        verify(bartenderRepository, times(1)).findById(3);
        verify(bartenderRepository, times(0)).deleteById(3);
    }
}
