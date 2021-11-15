package com.kti.restaurant.repository;

import com.kti.restaurant.model.PriceItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceItemRepository extends JpaRepository<PriceItem, Integer> {
}
