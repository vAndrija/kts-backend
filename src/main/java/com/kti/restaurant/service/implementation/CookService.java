package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.ConflictException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Cook;
import com.kti.restaurant.model.User;
import com.kti.restaurant.repository.CookRepository;
import com.kti.restaurant.repository.RoleRepository;
import com.kti.restaurant.repository.UserRepository;
import com.kti.restaurant.service.EmailService;
import com.kti.restaurant.service.contract.ICookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CookService implements ICookService {

    private CookRepository cookRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private EmailService emailService;

    @Autowired
    public CookService(CookRepository cookRepository, PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository, UserRepository userRepository, EmailService emailService){
        this.cookRepository = cookRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public List<Cook> findAll() {
        return cookRepository.findAll();
    }

    @Override
    public Cook findById(Integer id) throws Exception {
        Cook cook = cookRepository.findById(id).orElse(null);
        if (cook == null)
            throw new MissingEntityException("Cook with given id does not exist in the system.");
        return cook;
    }

    @Override
    public Cook create(Cook entity) throws Exception {
        User user = userRepository.findByEmailAddress(entity.getEmailAddress());
        if (user != null)
            throw new ConflictException("Cook with entered email already exists.");
        String tempPassword = UUID.randomUUID().toString();
        emailService.sendRegistrationMail(entity.getEmailAddress(),"Nalog kreiran", tempPassword);
        entity.setPassword(passwordEncoder.encode(tempPassword));
        entity.getRoles().add(roleRepository.getById(3L));
        cookRepository.save(entity);
        return entity;
    }

    @Override
    public Cook update(Cook entity, Integer id) throws Exception {
        Cook cook = this.findById(id);
        cook.setName(entity.getName());
        cook.setLastName(entity.getLastName());
        cook.setPhoneNumber(entity.getPhoneNumber());
        cook.setAccountNumber(entity.getAccountNumber());
        cook.setPriority(entity.getPriority());
        cookRepository.save(cook);
        return cook;
    }

    @Override
    public void delete(Integer id) throws Exception {
        Cook cook = this.findById(id);
        cookRepository.delete(cook);
    }

    @Override
    public Cook findByUserId(Integer userId) {
        return cookRepository.findById(userId).orElse(null);
    }
}
