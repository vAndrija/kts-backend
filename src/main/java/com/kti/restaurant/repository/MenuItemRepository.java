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
    List<MenuItem> findByNameAndDescription(String search);

    @Query("select mi from MenuItem mi join Menu m on mi.menu.id = m.id where mi.category=?1")
    List<MenuItem> findByCategory(MenuItemCategory category);

    Page<MenuItem> findByCategory(MenuItemCategory category, Pageable pageable);

    List<MenuItem> findByType(MenuItemType type);
    
    @Query("select mi from MenuItem mi where mi.accepted = false")
    Page<MenuItem> findPendingMenuItems(Pageable pageable);
  
    @Query("select mi from MenuItem mi where mi.menu.id = :id")
    Page<MenuItem> findByMenu(Integer id, Pageable pageable);

    Page<MenuItem> findAll(Pageable pageable);

    @Query("select mi from MenuItem mi where mi.accepted=true and mi.menu.id = :menuId and (:category is NULL or mi.category = :category) and (mi.name like lower(concat('%', :searchParam, '%')) or " +
            "mi.description like lower(concat('%', :searchParam, '%')))")
    Page<MenuItem> searchAndFilterMenuItems(Integer menuId, String searchParam, MenuItemCategory category, Pageable pageable);
}
