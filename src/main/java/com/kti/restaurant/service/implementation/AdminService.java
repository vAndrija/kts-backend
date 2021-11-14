package com.kti.restaurant.service.implementation;

import com.kti.restaurant.model.Admin;
import com.kti.restaurant.repository.AdminRepository;
import com.kti.restaurant.service.contract.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService implements IAdminService {

    private AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder){
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Admin> findAll() {
        return null;
    }

    @Override
    public Admin findById(Integer id) {
        return null;
    }

    @Override
    public Admin create(Admin entity) {
            //TODO promjeniti kada uvedemo global exception zastitit od pucanja zbog istom mejla
            entity.setPassword(passwordEncoder.encode(entity.getPassword()));
            adminRepository.save(entity);
            return entity;
    }

    @Override
    public Admin update(Admin entity) throws Exception {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
