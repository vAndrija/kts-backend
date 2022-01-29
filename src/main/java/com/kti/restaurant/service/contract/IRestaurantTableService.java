package com.kti.restaurant.service.contract;

import com.kti.restaurant.model.RestaurantTable;

public interface IRestaurantTableService extends IService<RestaurantTable> {

    boolean check(Integer x, Integer y);
}
