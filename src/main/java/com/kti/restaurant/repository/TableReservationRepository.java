package com.kti.restaurant.repository;

import com.kti.restaurant.model.TableReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableReservationRepository extends JpaRepository<TableReservation, Integer> {
}
