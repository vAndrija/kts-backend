package com.kti.restaurant.repository;

import com.kti.restaurant.model.Salary;
import com.kti.restaurant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SalaryRepository extends JpaRepository<Salary, Integer> {

    @Query("select s from Salary s where ?1 >= s.startDate and ?1 <= s.endDate and s.user.id=?2")
    Salary findSalaryForDate(LocalDate date, Integer userId);


    List<Salary> findAllByUser(User user);
}
