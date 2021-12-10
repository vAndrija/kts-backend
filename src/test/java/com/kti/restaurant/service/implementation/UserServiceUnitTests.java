package com.kti.restaurant.service.implementation;


import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Admin;
import com.kti.restaurant.model.User;
import com.kti.restaurant.repository.RoleRepository;
import com.kti.restaurant.repository.UserRepository;
import com.kti.restaurant.security.auth.ConfirmationTokenRepository;
import com.kti.restaurant.service.EmailService;
import com.kti.restaurant.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class UserServiceUnitTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        User user = new Admin("Vojnovic", "Andrija", "213123123", "andrija@vojnvo.com", "21312311");
        user.setId(1);
        when(userRepository.getByEmailAddress("andrija@vojnvo.com"))
                .thenReturn(user);
    }

    @Test
    public void loadUserByUsername_ValidUsername_ExistingUserDetails() throws Exception {
        UserDetails userDetails = userService.loadUserByUsername("andrija@vojnvo.com");
        assertEquals( "andrija@vojnvo.com",userDetails.getUsername());
    }

    @Test
    public void loadUserByUsername_InvalidUsername_ThrowsUsernameNotFoundException() throws Exception {
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("andrija@vojnovic.com");
        });
    }

    @Test
    public void sendResetToken_ValidEmail() throws Exception {

    }


}
