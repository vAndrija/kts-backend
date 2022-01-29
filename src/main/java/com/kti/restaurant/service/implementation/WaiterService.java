package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.ConflictException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Role;
import com.kti.restaurant.model.Salary;
import com.kti.restaurant.model.User;
import com.kti.restaurant.model.Waiter;
import com.kti.restaurant.repository.RoleRepository;
import com.kti.restaurant.repository.SalaryRepository;
import com.kti.restaurant.repository.UserRepository;
import com.kti.restaurant.repository.WaiterRepository;
import com.kti.restaurant.service.EmailService;
import com.kti.restaurant.service.contract.IWaiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class WaiterService implements IWaiterService {

    private WaiterRepository waiterRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private EmailService emailService;
    private SalaryRepository salaryRepository;

    @Autowired
    public WaiterService(WaiterRepository waiterRepository, PasswordEncoder passwordEncoder,
                         RoleRepository roleRepository, UserRepository userRepository,
                         EmailService emailService, SalaryRepository salaryRepository) {
        this.waiterRepository = waiterRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.salaryRepository = salaryRepository;
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
        String tempPassword = UUID.randomUUID().toString();
        emailService.sendRegistrationMail(entity.getEmailAddress(), "Nalog kreiran", tempPassword);
        entity.setPassword(passwordEncoder.encode(tempPassword));
        entity.getRoles().add(roleRepository.getById(5L));
        Waiter waiter = waiterRepository.save(entity);
        Salary salary = new Salary(45000.00, LocalDate.now(), LocalDate.now(), waiter);
        salaryRepository.save(salary);
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
