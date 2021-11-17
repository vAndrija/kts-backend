package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.ConflictException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Admin;
import com.kti.restaurant.model.Role;
import com.kti.restaurant.model.User;
import com.kti.restaurant.repository.AdminRepository;
import com.kti.restaurant.repository.RoleRepository;
import com.kti.restaurant.repository.UserRepository;
import com.kti.restaurant.service.contract.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService implements IAdminService {

    private AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder,
                        RoleRepository roleRepository,UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    @Override
    public Admin findById(Integer id) throws Exception {
        Admin admin = adminRepository.findById(id).orElse(null);
        if (admin == null)
            throw new MissingEntityException("Admin with given id does not exist in the system.");
        return admin;
    }

    @Override
    public Admin create(Admin entity) throws Exception {
        User user = userRepository.findByEmailAddress(entity.getEmailAddress());
        if (user != null)
            throw new ConflictException("Admin with entered email already exists.");
        System.out.println(entity.getPassword());
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.getRoles().add(roleRepository.getById(1L));
        adminRepository.save(entity);
        return entity;
    }

    @Override
    public Admin update(Admin entity, Integer id) throws Exception {
        Admin admin = this.findById(id);
        admin.setName(entity.getName());
        admin.setLastName(entity.getLastName());
        admin.setPhoneNumber(entity.getPhoneNumber());
        admin.setAccountNumber(entity.getAccountNumber());
        adminRepository.save(admin);
        return admin;
    }

    @Override
    public void delete(Integer id) throws Exception {
        Admin admin = this.findById(id);
        adminRepository.delete(admin);
    }
}
