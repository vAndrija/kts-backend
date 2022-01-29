package com.kti.restaurant.service.contract;

import com.kti.restaurant.model.Menu;

import java.time.LocalDateTime;
import java.util.List;

public interface IMenuService extends IService<Menu> {
    List<Menu> findMenusForDate(LocalDateTime date);
}
