package com.kti.restaurant.controller;

import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.dto.auth.ResetPasswordDTO;
import com.kti.restaurant.model.Admin;
import com.kti.restaurant.model.User;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.repository.UserRepository;
import com.kti.restaurant.security.auth.ConfirmationToken;
import com.kti.restaurant.security.auth.ConfirmationTokenRepository;
import com.kti.restaurant.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class AuthenticationControllerIntegrationTests {

    private static final String URL_PREFIX = "/api/v1/auth";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private HttpHeaders headers = new HttpHeaders();

    @Test
    public void createAuthenticationToken_ValidData_ReturnsOk() {
        HttpEntity<JwtAuthenticationRequest> httpEntity =
                new HttpEntity<>(new JwtAuthenticationRequest("mirkomiric@gmail.com", "123"), headers);
        ResponseEntity<UserTokenState> responseEntity = restTemplate.postForEntity(URL_PREFIX + "/login", httpEntity, UserTokenState.class);
        UserTokenState userTokenState = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotEquals(null, userTokenState.getAccessToken());
    }

    @Test
    public void createAuthenticationToken_InvalidData_ReturnsUnauthorized() {
        HttpEntity<JwtAuthenticationRequest> httpEntity =
                new HttpEntity<>(new JwtAuthenticationRequest("mirkomiric@gmail.com", "1233"), headers);
        ResponseEntity<UserTokenState> responseEntity = restTemplate.postForEntity(URL_PREFIX + "/login", httpEntity, UserTokenState.class);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void sendResetToken_ValidEmail_StatusOk() {
        int size = confirmationTokenRepository.findAll().size();
        HttpEntity<ResetPasswordDTO> httpEntity =
                new HttpEntity<>(new ResetPasswordDTO(null, null, "mirkomiric@gmail.com"), headers);
        ResponseEntity<Object> responseEntity =
                restTemplate.postForEntity(URL_PREFIX + "/send-reset-password-link", httpEntity, Object.class);
        int sizeAfter = confirmationTokenRepository.findAll().size();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(size + 1, sizeAfter);
        confirmationTokenRepository.delete(confirmationTokenRepository.findByUser(userService.findById(1)));

    }

    @Test
    public void sendResetToken_InvalidEmail_ReturnsNotFound() {
        int size = confirmationTokenRepository.findAll().size();
        HttpEntity<ResetPasswordDTO> httpEntity =
                new HttpEntity<>(new ResetPasswordDTO(null, null, "mirkomiric2131@gmail.com"), headers);
        ResponseEntity<Object> responseEntity =
                restTemplate.postForEntity(URL_PREFIX + "/send-reset-password-link", httpEntity, Object.class);
        int sizeAfter = confirmationTokenRepository.findAll().size();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(size, sizeAfter);
    }

    @Test
    public void resetPasswordDone_ValidData_ReturnsOk() {
        User user = userService.findById(1);
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);
        int size = confirmationTokenRepository.findAll().size();
        HttpEntity<ResetPasswordDTO> httpEntity =
                new HttpEntity<>(new ResetPasswordDTO(confirmationToken.getConfirmationToken(),
                        "1234", user.getEmailAddress()), headers);
        ResponseEntity<Object> responseEntity =
                restTemplate.postForEntity(URL_PREFIX + "/reset-password", httpEntity, Object.class);
        int sizeAfter = confirmationTokenRepository.findAll().size();
        User userChanged = userService.findById(1);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(size - 1, sizeAfter);
        assertTrue(passwordEncoder.matches("1234", userChanged.getPassword()));

        userChanged.setPassword(passwordEncoder.encode("123"));
        userRepository.save(userChanged);
    }

    @Test
    public void resetPasswordDone_InvalidToken_ReturnsForbidden() {
        Admin admin = new Admin("Aleksa", "Maric",
                "111111", "aleksamaric2311@gmail.com", "152487");
        ConfirmationToken confirmationToken = new ConfirmationToken(admin);
        int size = confirmationTokenRepository.findAll().size();
        HttpEntity<ResetPasswordDTO> httpEntity =
                new HttpEntity<>(new ResetPasswordDTO(confirmationToken.getConfirmationToken(),
                        "1234", null), headers);
        ResponseEntity<Object> responseEntity =
                restTemplate.postForEntity(URL_PREFIX + "/reset-password", httpEntity, Object.class);
        int sizeAfter = confirmationTokenRepository.findAll().size();
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals(size, sizeAfter);
    }

    @Test
    public void resetPasswordDone_ExpiredToken_ReturnsForbidden() {
        User user = userService.findById(1);
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationToken.setCreatedDate(new Date(new Date().getTime() - 1000 * 60 * 60 * 31));
        confirmationToken = confirmationTokenRepository.save(confirmationToken);

        int size = confirmationTokenRepository.findAll().size();
        HttpEntity<ResetPasswordDTO> httpEntity =
                new HttpEntity<>(new ResetPasswordDTO(confirmationToken.getConfirmationToken(),
                        "1234", user.getEmailAddress()), headers);
        ResponseEntity<Object> responseEntity =
                restTemplate.postForEntity(URL_PREFIX + "/reset-password", httpEntity, Object.class);
        int sizeAfter = confirmationTokenRepository.findAll().size();

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals(size, sizeAfter);
        confirmationTokenRepository.delete(confirmationToken);
    }
}
