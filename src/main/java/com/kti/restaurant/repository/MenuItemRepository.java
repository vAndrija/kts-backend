package com.kti.restaurant.repository;

import com.kti.restaurant.model.MenuItem;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface MenuItemRepository extends PagingAndSortingRepository<MenuItem, Integer> {
}
