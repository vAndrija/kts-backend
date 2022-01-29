package com.kti.restaurant.repository;

import com.kti.restaurant.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
    @Query("select m from Menu m where m.durationStart < :date and m.durationEnd > :date")
    List<Menu> findMenusForDate(LocalDateTime date);

}
