package com.kti.restaurant.service.contract;

import com.kti.restaurant.dto.report.MenuItemSalesDto;

import java.util.List;

public interface IReportService {
    public List<Double> mealDrinkCostsForYear(Integer year);
    public List<Double> mealDrinkCostsForMonth(Integer year, Integer month);
    public List<Integer> mealDrinkSalesForYear(Integer year, Integer menuItemId);
    public List<Integer> mealDrinkSalesForMonth(Integer year, Integer month, Integer menuItemId);
}
