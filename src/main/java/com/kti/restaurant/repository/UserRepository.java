package com.kti.restaurant.repository;

import com.kti.restaurant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User,Integer> {
    User getByEmailAddress(String s);

    User findByEmailAddress(String emailAddress);
}
