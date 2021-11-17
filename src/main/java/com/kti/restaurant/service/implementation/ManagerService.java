package com.kti.restaurant.service.implementation;


import com.kti.restaurant.exception.ConflictException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Manager;
import com.kti.restaurant.model.User;
import com.kti.restaurant.repository.ManagerRepository;
import com.kti.restaurant.repository.RoleRepository;
import com.kti.restaurant.repository.UserRepository;
import com.kti.restaurant.service.contract.IManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService implements IManagerService {

    private ManagerRepository managerRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    @Autowired
    public ManagerService(ManagerRepository managerRepository, PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository, UserRepository userRepository) {
        this.managerRepository = managerRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }


    @Override
    public List<Manager> findAll() {
        return managerRepository.findAll();
    }

    @Override
    public Manager findById(Integer id) throws Exception {
        Manager manager = managerRepository.findById(id).orElse(null);
        if (manager == null)
            throw new MissingEntityException("Manager with given id does not exist in the system.");
        return manager;
    }

    @Override
    public Manager create(Manager entity) throws Exception {
        User user = userRepository.findByEmailAddress(entity.getEmailAddress());
        if (user != null)
            throw new ConflictException("Manager with entered email already exists.");
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.getRoles().add(roleRepository.getById(4L));
        managerRepository.save(entity);
        return entity;
    }

    @Override
    public Manager update(Manager entity, Integer id) throws Exception {
        Manager manager = this.findById(id);
        manager.setName(entity.getName());
        manager.setLastName(entity.getLastName());
        manager.setPhoneNumber(entity.getPhoneNumber());
        manager.setAccountNumber(entity.getAccountNumber());
        managerRepository.save(manager);
        return manager;
    }

    @Override
    public void delete(Integer id) throws Exception {
        Manager manager = this.findById(id);
        managerRepository.delete(manager);
    }
}
