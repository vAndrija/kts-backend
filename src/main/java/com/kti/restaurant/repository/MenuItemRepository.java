package com.kti.restaurant.repository;

import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.model.enums.MenuItemType;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {

    List<MenuItem> findAll(Example exampleQuery);

    List<MenuItem> findByCategory(MenuItemCategory category);

    List<MenuItem> findByType(MenuItemType type);
}
