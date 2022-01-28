package com.kti.restaurant.repository;

import com.kti.restaurant.model.PriceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PriceItemRepository extends JpaRepository<PriceItem, Integer> {
    @Query("select pi from PriceItem pi where ?1 >= pi.startDate and (?1 < pi.endDate or pi.endDate is NULL) and pi.menuItem.id=?2")
    PriceItem findPriceItemForDate(LocalDate date, Integer menuItemId);
}
