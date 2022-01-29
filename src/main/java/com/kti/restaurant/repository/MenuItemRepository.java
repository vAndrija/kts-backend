package com.kti.restaurant.repository;

import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.model.enums.MenuItemType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {

    @Query("select mi from MenuItem mi join Menu m on mi.menu.id = m.id where mi.accepted = true " +
            "and m.durationStart<=:localDateTime and m.durationEnd>=:localDateTime and (lower(mi.name) like lower(concat('%', :search, '%')) or " +
            "lower(mi.description) like lower(concat('%', :search, '%')))")
    List<MenuItem> findByNameAndDescription(String search, LocalDateTime localDateTime);

    @Query("select mi from MenuItem mi join Menu m on mi.menu.id = m.id where mi.accepted = true and  mi.category=?1 and m.durationStart<=?2 and m.durationEnd>=?2")
    List<MenuItem> findByCategory(MenuItemCategory category, LocalDateTime localDateTime);

    @Query("select mi from MenuItem mi join Menu m on mi.menu.id = m.id where mi.accepted = true and  mi.category=?1 and m.durationStart<=?2 and m.durationEnd>=?2")
    Page<MenuItem> findByCategory(MenuItemCategory category, LocalDateTime localDateTime, Pageable pageable);

    @Query("select mi from MenuItem mi join Menu m on mi.menu.id = m.id where mi.accepted = true and  mi.type=?1 and m.durationStart<=?2 and m.durationEnd>=?2")
    List<MenuItem> findByType(MenuItemType type, LocalDateTime localDateTime);

    @Query("select mi from MenuItem mi where mi.accepted = false")
    Page<MenuItem> findPendingMenuItems(Pageable pageable);

    @Query("select mi from MenuItem mi where mi.menu.id = :id")
    Page<MenuItem> findByMenu(Integer id, Pageable pageable);

    @Query("select mi from MenuItem  mi where mi.accepted = true")
    Page<MenuItem> findAll(Pageable pageable);

    @Query("select mi from MenuItem  mi  join Menu m on mi.menu.id = m.id where mi.accepted = true and m.durationStart<=?1 and m.durationEnd>=?1")
    Page<MenuItem> findAllInActiveMenu(LocalDateTime dateTime, Pageable pageable);

    @Query("select mi from MenuItem mi where mi.accepted=true and mi.menu.id = :menuId and (:category is NULL or mi.category = :category) and (mi.name like lower(concat('%', :searchParam, '%')) or " +
            "mi.description like lower(concat('%', :searchParam, '%')))")
    Page<MenuItem> searchAndFilterMenuItems(Integer menuId, String searchParam, MenuItemCategory category, Pageable pageable);
}
