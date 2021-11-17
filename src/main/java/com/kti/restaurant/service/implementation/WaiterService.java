package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.ConflictException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Role;
import com.kti.restaurant.model.User;
import com.kti.restaurant.model.Waiter;
import com.kti.restaurant.repository.RoleRepository;
import com.kti.restaurant.repository.UserRepository;
import com.kti.restaurant.repository.WaiterRepository;
import com.kti.restaurant.service.contract.IWaiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaiterService  implements IWaiterService {

    private WaiterRepository waiterRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    @Autowired
    public WaiterService(WaiterRepository waiterRepository, PasswordEncoder passwordEncoder,
                         RoleRepository roleRepository, UserRepository userRepository) {
        this.waiterRepository = waiterRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Waiter> findAll() {
        return this.waiterRepository.findAll();
    }

    @Override
    public Waiter findById(Integer id) throws Exception {
        Waiter waiter = waiterRepository.findById(id).orElse(null);
        if (waiter == null)
            throw new MissingEntityException("Waiter with given id does not exist in the system.");
        return waiter;
    }

    @Override
    public Waiter create(Waiter entity) throws Exception {
        User user = userRepository.findByEmailAddress(entity.getEmailAddress());
        if (user != null)
            throw new ConflictException("Waiter with entered email already exists.");
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.getRoles().add(roleRepository.getById(5L));
        waiterRepository.save(entity);
        return entity;
    }

    @Override
    public Waiter update(Waiter entity, Integer id) throws Exception {
        Waiter waiter = this.findById(id);
        waiter.setName(entity.getName());
        waiter.setLastName(entity.getLastName());
        waiter.setPhoneNumber(entity.getPhoneNumber());
        waiter.setAccountNumber(entity.getAccountNumber());
        waiterRepository.save(waiter);
        return waiter;
    }

    @Override
    public void delete(Integer id) throws Exception {
        Waiter waiter = this.findById(id);
        waiterRepository.delete(waiter);
    }
}
