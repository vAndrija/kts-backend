package com.kti.restaurant.service.contract;

import com.kti.restaurant.model.MenuItem;

import java.util.Set;

public interface IMenuItemService extends IService<MenuItem> {

    Set<MenuItem> search(String s);
    Set<MenuItem> filter(String f);

}
