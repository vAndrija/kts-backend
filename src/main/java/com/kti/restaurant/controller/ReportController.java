package com.kti.restaurant.controller;

import com.kti.restaurant.service.contract.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/reports")
public class ReportController {
    private IReportService reportService;

    @Autowired
    public ReportController(IReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/yearly/{year}/meal-drink-costs")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<List<Double>> getYearlyReportForMealAndDrinkCosts(@PathVariable Integer year) {
        return new ResponseEntity<List<Double>>(reportService.mealDrinkCostsForYear(year), HttpStatus.OK);
    }

    @GetMapping("{year}/monthly/{month}/meal-drink-costs")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<List<Double>> getMonthlyReportForMealAndDrinkCosts(@PathVariable Integer year, @PathVariable Integer month) {
        return new ResponseEntity<List<Double>>(reportService.mealDrinkCostsForMonth(year, month), HttpStatus.OK);
    }

    @GetMapping("/yearly/{year}/meal-drink-sales/{id}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<List<Integer>> getYearlyReportForMealAndDrinkSales(@PathVariable Integer year, @PathVariable Integer id) throws Exception {
        return new ResponseEntity<List<Integer>>(reportService.mealDrinkSalesForYear(year, id), HttpStatus.OK);
    }

    @GetMapping("/{year}/monthly/{month}/meal-drink-sales/{id}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<List<Integer>> getMonthlyReportForMealAndDrinkSales(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer id) throws Exception {
        return new ResponseEntity<List<Integer>>(reportService.mealDrinkSalesForMonth(year, month, id), HttpStatus.OK);
    }

    @GetMapping("/yearly/{year}/cost-benefit-ratio")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<List<Double>> getYearlyCostBenefitRatio(@PathVariable Integer year) {
        return new ResponseEntity<List<Double>>(reportService.costBenefitRatioForYear(year), HttpStatus.OK);
    }

    @GetMapping("{year}/monthly/{month}/cost-benefit-ratio")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<List<Double>> getMonthlyReportForCostBenefitRatio(@PathVariable Integer year, @PathVariable Integer month) {
        return new ResponseEntity<List<Double>>(reportService.costBenefitRatioForMonth(year, month), HttpStatus.OK);
    }

    @GetMapping("/yearly/{year}/time-preparing/{userId}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<List<Integer>> getYearlyPreparingTimeReport(@PathVariable Integer year, @PathVariable Integer userId) {
        return new ResponseEntity<List<Integer>>(reportService.preparationTimeForYear(year, userId), HttpStatus.OK);
    }

    @GetMapping("{year}/monthly/{month}/time-preparing{userId}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<List<Integer>> getMonthlyPreparingTimeReport(@PathVariable Integer year, @PathVariable Integer month,
                                                                      @PathVariable Integer userId) {
        return new ResponseEntity<List<Integer>>(reportService.preparationTimeForMonth(year, month, userId), HttpStatus.OK);
    }
}
