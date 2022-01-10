package com.kti.restaurant.service.contract;

import com.kti.restaurant.model.MenuItem;
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

    List<MenuItem> findByMenu(Integer menuId, Pageable pageable) throws Exception;

    Page<MenuItem> findAll(Pageable pageable);
}
