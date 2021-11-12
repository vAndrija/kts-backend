package com.kti.restaurant.controller;


import antlr.Token;
import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.model.User;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.security.TokenUtils;
import com.kti.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

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

        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        System.out.println(authenticationRequest.getUsername()+" "+ authenticationRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
        // kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token za tog korisnika
        User user = (User) authentication.getPrincipal();
        String role = user.getRoles().get(0).getName();
        System.out.println(role);
        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();
        // Vrati token kao odgovor na uspesnu autentifikaciju
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
    }
}