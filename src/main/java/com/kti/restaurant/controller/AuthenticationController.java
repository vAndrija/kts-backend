package com.kti.restaurant.controller;


import antlr.Token;
import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.dto.auth.ResetPasswordDTO;
import com.kti.restaurant.model.User;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.security.TokenUtils;
import com.kti.restaurant.service.UserService;
import org.hibernate.annotations.GeneratorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping(value="/api/v1/auth")
public class AuthenticationController {

    private UserService userService;
    private TokenUtils tokenUtils;
    private AuthenticationManager authenticationManager;


    @Autowired
    public AuthenticationController(UserService userService, TokenUtils tokenUtils, AuthenticationManager authenticationManager){
        this.userService = userService;
        this.tokenUtils = tokenUtils;
        this.authenticationManager  = authenticationManager;
    }


    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {
        System.out.println(authenticationRequest.getUsername()+" "+ authenticationRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        String role = user.getRoles().get(0).getName();
        Integer id = user.getId();
        String jwt = tokenUtils.generateToken(user.getUsername(),role, id);
        int expiresIn = tokenUtils.getExpiredIn();
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn,role, id));
    }

    @PostMapping("/send-reset-password-link")
    public ResponseEntity<?> sendResetToken(@RequestBody ResetPasswordDTO resetPasswordDTO) throws Exception {
        this.userService.sendResetToken(resetPasswordDTO.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public void resetPasswordDone(@RequestBody ResetPasswordDTO resetPasswordDTO) throws Exception {
        this.userService.resetPasswordDone(resetPasswordDTO.getToken(),resetPasswordDTO.getPassword());
    }

}
