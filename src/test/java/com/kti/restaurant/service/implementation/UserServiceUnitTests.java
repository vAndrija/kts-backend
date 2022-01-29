package com.kti.restaurant.service.implementation;


import com.kti.restaurant.exception.BadTokenException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Admin;
import com.kti.restaurant.model.User;
import com.kti.restaurant.repository.UserRepository;
import com.kti.restaurant.security.auth.ConfirmationToken;
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

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        when(userRepository.findByEmailAddress("andrija@vojnvo.com"))
                .thenReturn(user);
    }

    @Test
    public void loadUserByUsername_ValidUsername_ReturnsExistingUserDetails() throws Exception {
        UserDetails userDetails = userService.loadUserByUsername("andrija@vojnvo.com");
        assertEquals("andrija@vojnvo.com", userDetails.getUsername());
    }

    @Test
    public void loadUserByUsername_InvalidUsername_ThrowsUsernameNotFoundException() throws Exception {
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("andrija@vojnovic.com");
        });
    }

    @Test
    public void sendResetToken_ValidEmail() throws Exception {
        when(confirmationTokenRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));
        assertDoesNotThrow(() -> {
            userService.sendResetToken("andrija@vojnvo.com");
        });
    }

    @Test
    public void sendResetToken_InvalidEmail_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            userService.sendResetToken("andrija@vojnovic.com");
        });
    }

    @Test
    public void resetPasswordDone_ValidData() throws Exception {
        User user = new Admin("Vojnovic", "Andrija", "213123123", "andrija@vojnvo.com", "21312311");
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        when(confirmationTokenRepository.findByConfirmationToken(any()))
                .thenAnswer(a -> confirmationToken);
        userService.resetPasswordDone(confirmationToken.getConfirmationToken(), "modemmodem123");
        verify(confirmationTokenRepository, times(1)).delete(confirmationToken);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void resetPasswordDone_UnexistingToken_ThrowsBadTokenException() throws Exception {
        User user = new Admin("Vojnovic", "Andrija", "213123123", "andrija@vojnvo.com", "21312311");
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        when(confirmationTokenRepository.findByConfirmationToken(any()))
                .thenAnswer(a -> null);
        BadTokenException exception = assertThrows(BadTokenException.class, () -> {
            userService.resetPasswordDone(confirmationToken.getConfirmationToken(), "modemmodem123");
        });
        assertEquals("Bad token", exception.getMessage());
        verify(confirmationTokenRepository, times(0)).delete(confirmationToken);
        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void resetPasswordDone_ExpiredToken_ThrowsBadTokenException() throws Exception {
        User user = new Admin("Vojnovic", "Andrija", "213123123", "andrija@vojnvo.com", "21312311");
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        when(confirmationTokenRepository.findByConfirmationToken(any()))
                .thenAnswer(a -> confirmationToken);
        confirmationToken.setCreatedDate(new Date(new Date().getTime() - 1000 * 60 * 60 * 31));
        BadTokenException exception = assertThrows(BadTokenException.class, () -> {
            userService.resetPasswordDone(confirmationToken.getConfirmationToken(), "modemmodem123");
        });
        assertEquals("Token expired", exception.getMessage());
        verify(confirmationTokenRepository, times(0)).delete(confirmationToken);
        verify(userRepository, times(0)).save(any());
    }
    
    @Test
    public void findUserByUsername_ValidUserEmail_ReturnsUser() {
    	User user = userService.findUserByUsername("andrija@vojnvo.com");
    	assertEquals("andrija@vojnvo.com", user.getEmailAddress());
    }
    
    @Test
    public void findUserByUsername_InvalidUserEmail_ThrowsMissingEntityException() {
    	when(userRepository.findByEmailAddress("invalid")).thenThrow(MissingEntityException.class);
    	
    	assertThrows(MissingEntityException.class, ()-> {
    		userService.findUserByUsername("invalid");
    	});
    }

}
