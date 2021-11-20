package com.kti.restaurant.repository;

import com.kti.restaurant.model.Waiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaiterRepository extends JpaRepository<Waiter,Integer> {
    Waiter findByEmailAddress(String emailAddress);
}
