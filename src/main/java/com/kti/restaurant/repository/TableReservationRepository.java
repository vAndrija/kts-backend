package com.kti.restaurant.repository;

import com.kti.restaurant.model.TableReservation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TableReservationRepository extends JpaRepository<TableReservation, Integer> {
	@Query("select tr from TableReservation tr join RestaurantTable rt on tr.table.id = rt.id where tr.table.id = ?1 and tr.durationEnd >= ?2 and tr.durationStart <= ?3")
	List<TableReservation> getTableReservationByDateAndTableId(Integer id, LocalDateTime startDate, LocalDateTime endDate);
}
