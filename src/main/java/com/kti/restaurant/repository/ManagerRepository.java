package com.kti.restaurant.repository;

import com.kti.restaurant.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager,Integer> {

    Manager findByEmailAddress(String emailAddress);
}
