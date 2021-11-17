package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.ConflictException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Bartender;
import com.kti.restaurant.model.User;
import com.kti.restaurant.repository.BartenderRepository;
import com.kti.restaurant.repository.RoleRepository;
import com.kti.restaurant.repository.UserRepository;
import com.kti.restaurant.service.contract.IBartenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BartenderService implements IBartenderService {

    private BartenderRepository bartenderRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    @Autowired
    public BartenderService(BartenderRepository bartenderRepository, PasswordEncoder passwordEncoder,
                            RoleRepository roleRepository,UserRepository userRepository) {
        this.bartenderRepository = bartenderRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Bartender> findAll() {
        return bartenderRepository.findAll();
    }

    @Override
    public Bartender findById(Integer id) throws Exception {
        Bartender bartender = bartenderRepository.findById(id).orElse(null);
        if (bartender == null)
            throw new MissingEntityException("Bartender with given id does not exist in the system.");
        return bartender;
    }

    @Override
    public Bartender create(Bartender entity) throws Exception {
        User user = userRepository.findByEmailAddress(entity.getEmailAddress());
        if (user != null)
            throw new ConflictException("Bartender with entered email already exists.");
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.getRoles().add(roleRepository.getById(2L));
        bartenderRepository.save(entity);
        return entity;
    }

    @Override
    public Bartender update(Bartender entity, Integer id) throws Exception {
        Bartender bartender = this.findById(id);
        bartender.setName(entity.getName());
        bartender.setLastName(entity.getLastName());
        bartender.setPhoneNumber(entity.getPhoneNumber());
        bartender.setAccountNumber(entity.getAccountNumber());
        bartender.setPriority(entity.getPriority());
        bartenderRepository.save(bartender);
        return bartender;
    }

    @Override
    public void delete(Integer id) throws Exception {
        Bartender bartender = this.findById(id);
        bartenderRepository.delete(bartender);
    }
}