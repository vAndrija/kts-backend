package com.kti.restaurant.service.contract;

import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.model.enums.MenuItemCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMenuItemService extends IService<MenuItem> {

    Set<MenuItem> search(String s);

    Set<MenuItem> filter(String f);

    Page<MenuItem> filterPageable(String f, Pageable pageable);

    Page<MenuItem> pendingMenuItems(Pageable pageable);

    Page<MenuItem> findByMenu(Integer menuId, Pageable pageable) throws Exception;

    Page<MenuItem> findAll(Pageable pageable);

    Page<MenuItem> searchAndFilterMenuItems(Integer menuId, String searchParam, String category, Pageable pageable) throws Exception;
}
