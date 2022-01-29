package com.kti.restaurant.service.contract;

import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.model.enums.MenuItemCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMenuItemService extends IService<MenuItem> {

    Set<MenuItem> search(String s, LocalDateTime localDateTime);

    Set<MenuItem> filter(String f, LocalDateTime localDateTime);

    Page<MenuItem> filterPageable(String f, Pageable pageable, LocalDateTime localDateTime);

    Page<MenuItem> pendingMenuItems(Pageable pageable);

    Page<MenuItem> findByMenu(Integer menuId, Pageable pageable) throws Exception;

    Page<MenuItem> findAll(Pageable pageable);

    Page<MenuItem> findAllInActiveMenu(Pageable pageable, LocalDateTime localDateTime);

    Page<MenuItem> searchAndFilterMenuItems(Integer menuId, String searchParam, String category, Pageable pageable) throws Exception;
}
