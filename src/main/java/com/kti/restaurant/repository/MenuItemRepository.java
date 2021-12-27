package com.kti.restaurant.repository;

import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.model.enums.MenuItemType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
    
    @Query("select mi from MenuItem mi where lower(mi.name) like lower(concat('%', :search, '%')) or lower(mi.description) like lower(concat('%', :search, '%'))")
    List<MenuItem> findByNameAndDecription(String search);

    List<MenuItem> findByCategory(MenuItemCategory category);

    List<MenuItem> findByType(MenuItemType type);
    
    @Query("select mi from MenuItem mi where mi.accepted = false")
    Page<MenuItem> findPendingMenuItems(Pageable pageable);
}
