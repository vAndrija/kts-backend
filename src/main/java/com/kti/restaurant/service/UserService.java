package com.kti.restaurant.service;


import com.kti.restaurant.dto.auth.ResetPasswordDTO;
import com.kti.restaurant.exception.BadTokenException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.User;
import com.kti.restaurant.repository.UserRepository;
import com.kti.restaurant.security.auth.ConfirmationToken;
import com.kti.restaurant.security.auth.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService  implements UserDetailsService {

    private UserRepository userRepository;
    private ConfirmationTokenRepository confirmationTokenRepository;
    private EmailService emailService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, ConfirmationTokenRepository confirmationTokenRepository,
                       EmailService emailService, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.getByEmailAddress(email);
    }


    public void sendResetToken(String email) throws Exception{
        User user = userRepository.getByEmailAddress(email);
        if(user == null) {
            throw new MissingEntityException("User with given email does not exist in the system.");
        }
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);
        emailService.sendResetLinkMail(user.getEmailAddress(),"Zaboravljena lozinka",confirmationToken.getConfirmationToken());
    }

    public void resetPasswordDone(String email, String confirmationToken, String password) throws Exception {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if(token == null)
            throw new BadTokenException("Bad token");
        long differenceInTime = (new Date().getTime() - token.getCreatedDate().getTime())/(1000*60*60);
        if(differenceInTime>30)
            throw new BadTokenException("Token expired");
        confirmationTokenRepository.delete(token);
        User user = userRepository.getByEmailAddress(token.getUser().getEmailAddress());
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
}
