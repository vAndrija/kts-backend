package com.kti.restaurant.repository;

import com.kti.restaurant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmailAddress(String emailAddress);
}
