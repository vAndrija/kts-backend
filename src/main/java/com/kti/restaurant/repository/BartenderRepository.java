package com.kti.restaurant.repository;

import com.kti.restaurant.model.Bartender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BartenderRepository  extends JpaRepository<Bartender,Integer> {
    Bartender findByEmailAddress(String emailAddress);
}
