package com.kti.restaurant.service.implementation;

import com.kti.restaurant.model.OrderItem;
import com.kti.restaurant.model.PriceItem;
import com.kti.restaurant.model.Salary;
import com.kti.restaurant.model.User;
import com.kti.restaurant.service.UserService;
import com.kti.restaurant.service.contract.IOrderItemService;
import com.kti.restaurant.service.contract.IPriceItemService;
import com.kti.restaurant.service.contract.IReportService;
import com.kti.restaurant.service.contract.ISalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService implements IReportService {
    private IOrderItemService orderItemService;
    private IPriceItemService priceItemService;
    private ISalaryService salaryService;
    private UserService userService;

    @Autowired
    public  ReportService(IOrderItemService orderItemService, IPriceItemService priceItemService,
                          ISalaryService salaryService, UserService userService) {
        this.orderItemService = orderItemService;
        this.priceItemService = priceItemService;
        this.salaryService = salaryService;
        this.userService = userService;
    }

    @Override
    public List<Double> mealDrinkCostsForYear(Integer year) {
        List<Double> costsPerMonths = new ArrayList<Double>(Collections.nCopies(12, 0.0));

        LocalDateTime firstDayInYearLocal = getDayInYear(year, 0, 1);
        LocalDateTime lastDayInYearLocal = getDayInYear(year, 11, 31);

        List<OrderItem> orderItemsInYear = orderItemService.findOrderItemsInPeriod(firstDayInYearLocal, lastDayInYearLocal);
        orderItemsInYear.forEach(orderItem -> {
            PriceItem priceItem = priceItemService.findPriceForDate(orderItem.getOrder().getDateOfOrder().toLocalDate(),
                    orderItem.getMenuItem().getId());

            if(priceItem != null){
                Integer month = orderItem.getOrder().getDateOfOrder().getMonthValue();
                costsPerMonths.set(month-1, costsPerMonths.get(month-1) + orderItem.getQuantity()*priceItem.getPreparationValue());
            }
        });

        return costsPerMonths;
    }

    @Override
    public List<Double> mealDrinkCostsForMonth(Integer year, Integer month) {
        Integer numberDaysInMonth = getNumberDaysInMonth(year, month-1);

        List<Double> costsPerDays = new ArrayList<Double>(Collections.nCopies(numberDaysInMonth, 0.0));

        LocalDateTime firstDayInMonthLocal = getDayInYear(year, month-1, 1);
        LocalDateTime lastDayInMonthLocal = getDayInYear(year, month-1, numberDaysInMonth);

        List<OrderItem> orderItemsInMonth = orderItemService.findOrderItemsInPeriod(firstDayInMonthLocal, lastDayInMonthLocal);
        orderItemsInMonth.forEach(orderItem -> {
            PriceItem priceItem = priceItemService.findPriceForDate(orderItem.getOrder().getDateOfOrder().toLocalDate(),
                    orderItem.getMenuItem().getId());

            if(priceItem != null){
                Integer day = orderItem.getOrder().getDateOfOrder().getDayOfMonth();
                costsPerDays.set(day-1, costsPerDays.get(day-1) + orderItem.getQuantity()*priceItem.getPreparationValue());
            }
        });

        return costsPerDays;
    }

    @Override
    public List<Double> costBenefitRatioForYear(Integer year) {
        List<Double> ratioForMonths = new ArrayList<Double>(Collections.nCopies(12, 0.0));

        LocalDateTime firstDayInYearLocal = getDayInYear(year, 0, 1);
        LocalDateTime lastDayInYearLocal = getDayInYear(year, 11, 31);

        List<OrderItem> orderItemsInYear = orderItemService.findOrderItemsInPeriod(firstDayInYearLocal, lastDayInYearLocal);
        orderItemsInYear.forEach(orderItem -> {
            PriceItem priceItem = priceItemService.findPriceForDate(orderItem.getOrder().getDateOfOrder().toLocalDate(),
                    orderItem.getMenuItem().getId());

            if(priceItem != null){
                Integer month = orderItem.getOrder().getDateOfOrder().getMonthValue();
                ratioForMonths.set(month-1, ratioForMonths.get(month-1) - orderItem.getQuantity()*priceItem.getPreparationValue() +
                        orderItem.getQuantity()*priceItem.getValue());
            }
        });

        List<User> users = userService.findAll();
        users.forEach(user -> {
            Salary salary = null;
            for (int i = 0; i < 12; i++) {
                LocalDate nextMonth = getDayInYear(year, i, 1).toLocalDate();
                if (salary == null || !(salary.getStartDate().isBefore(nextMonth) && salary.getEndDate().isAfter(nextMonth))) {
                    salary = salaryService.findSalaryForDate(nextMonth, user.getId());
                }
                if(salary != null) {
                    ratioForMonths.set(i, ratioForMonths.get(i) - salary.getValue());
                }
            }
        });

        return ratioForMonths;
    }

    @Override
    public List<Double> costBenefitRatioForMonth(Integer year, Integer month) {
        Integer numberDaysInMonth = getNumberDaysInMonth(year, month-1);

        LocalDateTime firstDayInMonthLocal = getDayInYear(year, month-1, 1);
        LocalDateTime lastDayInMonthLocal = getDayInYear(year, month-1, numberDaysInMonth);

        List<Double> ratioForMonths = new ArrayList<Double>(Collections.nCopies(numberDaysInMonth, 0.0));

        List<OrderItem> orderItemsInYear = orderItemService.findOrderItemsInPeriod(firstDayInMonthLocal, lastDayInMonthLocal);
        orderItemsInYear.forEach(orderItem -> {
            PriceItem priceItem = priceItemService.findPriceForDate(orderItem.getOrder().getDateOfOrder().toLocalDate(),
                    orderItem.getMenuItem().getId());

            if(priceItem != null){
                Integer day = orderItem.getOrder().getDateOfOrder().getDayOfMonth();
                ratioForMonths.set(day-1, ratioForMonths.get(day-1) - orderItem.getQuantity()*priceItem.getPreparationValue() +
                        orderItem.getQuantity()*priceItem.getValue());
            }
        });

        List<User> users = userService.findAll();
        users.forEach(user -> {
            Salary salary = salaryService.findSalaryForDate(firstDayInMonthLocal.toLocalDate(), user.getId());
            if(salary != null) {
                ratioForMonths.stream().map(value -> value - salary.getValue()/numberDaysInMonth);
            }
        });

        return ratioForMonths;
    }

    private LocalDateTime getDayInYear(Integer year, Integer month, Integer day) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        Date date = calendar.getTime();
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private Integer getNumberDaysInMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return calendar.getActualMaximum(Calendar.DATE);
    }
}
