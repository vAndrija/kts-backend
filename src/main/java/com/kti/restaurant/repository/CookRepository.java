package com.kti.restaurant.repository;

import com.kti.restaurant.model.Cook;
import com.kti.restaurant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CookRepository extends JpaRepository<Cook,Integer> {
    Cook findByEmailAddress(String emailAddress);
}
