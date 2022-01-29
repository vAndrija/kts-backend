package com.kti.restaurant.service.contract;

import com.kti.restaurant.model.Salary;

import java.time.LocalDate;

public interface ISalaryService extends IService<Salary> {
    public Salary findSalaryForDate(LocalDate date, Integer userId);
}
