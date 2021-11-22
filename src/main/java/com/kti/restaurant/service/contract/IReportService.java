package com.kti.restaurant.service.contract;

import java.util.List;

public interface IReportService {
    public List<Double> mealDrinkCostsForYear(Integer year);
    public List<Double> mealDrinkCostsForMonth(Integer year, Integer month);
    public List<Double> costBenefitRatioForYear(Integer year);
    public List<Double> costBenefitRatioForMonth(Integer year, Integer month);
}
