package com.kti.restaurant.service.implementation;


import com.kti.restaurant.exception.BadTokenException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.User;
import com.kti.restaurant.security.auth.ConfirmationToken;
import com.kti.restaurant.security.auth.ConfirmationTokenRepository;
import com.kti.restaurant.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class UserServiceIntegrationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Test
    public void loadUserByUsername_ValidUsername_ValidUser() throws Exception {
        UserDetails userDetails = userService.loadUserByUsername("mirkomiric@gmail.com");
        assertEquals("mirkomiric@gmail.com", userDetails.getUsername());
        assertEquals("$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra", userDetails.getPassword());
    }

    @Test
    public void loadUserByUsername_InvalidUsername_ThrowsUsernameNotFoundException() {
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("Andrija@sa.com");
        });
    }

    @Test
    @Rollback
    public void sendResetToken_ValidUser() throws Exception {
        int size = confirmationTokenRepository.findAll().size();
        User user = userService.findById(1);
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByUser(user);
        assertEquals(null, confirmationToken);
        userService.sendResetToken(user.getEmailAddress());
        int sizeAfter = confirmationTokenRepository.findAll().size();
        ConfirmationToken confirmationToken1 = confirmationTokenRepository.findByUser(user);
        assertNotEquals(null, confirmationToken1);
        assertEquals(size + 1, sizeAfter);
    }

    @Test
    public void sendResetToken_InvalidUser_ThrowsMissingEntityException() {
        int size = confirmationTokenRepository.findAll().size();
        assertThrows(MissingEntityException.class, () -> {
            userService.sendResetToken("and@com.com");
        });
        int sizeAfter = confirmationTokenRepository.findAll().size();
        assertEquals(size, sizeAfter);
    }

    @Test
    @Rollback
    public void resetPasswordDone_ValidData() throws Exception {
        User user = userService.findById(1);
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);
        int size = confirmationTokenRepository.findAll().size();
        String oldPassword = user.getPassword();
        userService.resetPasswordDone(confirmationToken.getConfirmationToken(), "225883");
        int sizeAfter = confirmationTokenRepository.findAll().size();
        User userAfter = userService.findById(1);
        assertEquals(size - 1, sizeAfter);
        assertNotEquals(oldPassword, userAfter.getPassword());
    }

    @Test
    @Rollback
    public void resetPasswordDone_InvalidToken_ThrowsBadTokenException() {
        User user = userService.findById(1);
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        String oldPassword = user.getPassword();
        Exception exception = assertThrows(BadTokenException.class, () -> {
            userService.resetPasswordDone(confirmationToken.getConfirmationToken(), "225883");
        });
        User userAfter = userService.findById(1);
        assertEquals("Bad token", exception.getMessage());
        assertEquals(oldPassword, userAfter.getPassword());
    }

    @Test
    @Rollback
    public void resetPasswordDone_ExpiredToken_ThrowsBadTokenException() {
        User user = userService.findById(1);
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationToken.setCreatedDate(new Date(new Date().getTime() - 1000 * 60 * 60 * 31));
        confirmationTokenRepository.save(confirmationToken);
        String oldPassword = user.getPassword();
        Exception exception = assertThrows(BadTokenException.class, () -> {
            userService.resetPasswordDone(confirmationToken.getConfirmationToken(), "225883");
        });
        User userAfter = userService.findById(1);
        assertEquals("Token expired", exception.getMessage());
        assertEquals(oldPassword, userAfter.getPassword());
    }
}
