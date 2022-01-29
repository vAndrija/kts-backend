package com.kti.restaurant.service.contract;

import com.kti.restaurant.model.PriceItem;

import java.time.LocalDate;

public interface IPriceItemService extends IService<PriceItem> {
    PriceItem findPriceForDate(LocalDate date, Integer menuItemId);
}
