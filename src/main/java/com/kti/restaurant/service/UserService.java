package com.kti.restaurant.service;


import com.kti.restaurant.dto.auth.ChangePasswordDto;
import com.kti.restaurant.exception.BadTokenException;
import com.kti.restaurant.exception.ConflictException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Admin;
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
import java.util.List;

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
        UserDetails userDetails = userRepository.findByEmailAddress(email);
        if(userDetails==null)
            throw new UsernameNotFoundException(email);
        return userDetails;
    }

    public User findUserByUsername(String email) {
    	User user = userRepository.findByEmailAddress(email);
    	if(user == null) {
            throw new MissingEntityException("User with given email does not exist in the system.");
    	}
    	return user;
    }

    public void sendResetToken(String email) throws Exception{
        User user = userRepository.findByEmailAddress(email);
        if(user == null) {
            throw new MissingEntityException("User with given email does not exist in the system.");
        }
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);
        emailService.sendResetLinkMail(user.getEmailAddress(),"Zaboravljena lozinka",confirmationToken.getConfirmationToken());
    }

    public void resetPasswordDone(String confirmationToken, String password) throws Exception {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if(token == null)
            throw new BadTokenException("Bad token");
        long differenceInTime = (new Date().getTime() - token.getCreatedDate().getTime())/(1000*60*60);
        if(differenceInTime>30)
            throw new BadTokenException("Token expired");
        confirmationTokenRepository.delete(token);
        User user = userRepository.findByEmailAddress(token.getUser().getEmailAddress());
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            throw new MissingEntityException("User with given id does not exist in the system.");
        return user;
    }

    public void changePassword(User user, ChangePasswordDto changePasswordDto) {
        if(!passwordEncoder.matches(changePasswordDto.getOldPassword(),user.getPassword()))
            throw new ConflictException("Bad old password");
        user.setPassword(passwordEncoder.encode(changePasswordDto.getPassword()));
        this.userRepository.save(user);
    }
}
