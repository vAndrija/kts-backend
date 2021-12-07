package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.BadLogicException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.*;
import com.kti.restaurant.service.UserService;
import com.kti.restaurant.service.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class ReportService implements IReportService {
    private IOrderItemService orderItemService;
    private IPriceItemService priceItemService;
    private ISalaryService salaryService;
    private UserService userService;
    private IMenuItemService menuItemService;
  
    @Autowired
    public  ReportService(IOrderItemService orderItemService, IPriceItemService priceItemService,
                          ISalaryService salaryService, UserService userService, MenuItemService menuItemService) {
        this.orderItemService = orderItemService;
        this.priceItemService = priceItemService;
        this.salaryService = salaryService;
        this.userService = userService;
        this.menuItemService = menuItemService;
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
    public List<Integer> mealDrinkSalesForYear(Integer year, Integer menuItemId) throws Exception {
        if(year < 0) {
        	throw new BadLogicException("Year cannot be negative value.");
        }
        
        menuItemService.findById(menuItemId);
    	
    	List<Integer> salesPerMonths = new ArrayList<Integer>(Collections.nCopies(12, 0));

        LocalDateTime firstDayInYearLocal = getDayInYear(year, 0, 1);
        LocalDateTime lastDayInYearLocal = getDayInYear(year, 11, 31);
 
        List<OrderItem> orderItemsInYear = orderItemService.findOrderItemsInPeriodForMenuItem(firstDayInYearLocal,
                lastDayInYearLocal, menuItemId);

        orderItemsInYear.forEach(orderItem -> {
                Integer month = orderItem.getOrder().getDateOfOrder().getMonthValue();
                salesPerMonths.set(month-1, salesPerMonths.get(month-1) + orderItem.getQuantity());
        });
        
        return salesPerMonths;
    }

    @Override
    public List<Integer> mealDrinkSalesForMonth(Integer year, Integer month, Integer menuItemId) throws Exception {
    	if(year < 0) {
    		throw new BadLogicException("Year cannot be negative value.");
    	}
    	
    	if(month < 1 || month > 12) {
    		throw new BadLogicException("Month needs to be in range 1 and 12");
    	}
    	
    
    	menuItemService.findById(menuItemId);
    	
        
        Integer numberDaysInMonth = getNumberDaysInMonth(year, month-1);
        List<Integer> salesPerDays = new ArrayList<Integer>(Collections.nCopies(numberDaysInMonth, 0));

        LocalDateTime firstDayInMonthLocal = getDayInYear(year, month-1, 1);
        LocalDateTime lastDayInMonthLocal = getDayInYear(year, month-1, numberDaysInMonth);

        List<OrderItem> orderItemsInYear = orderItemService.findOrderItemsInPeriodForMenuItem(firstDayInMonthLocal,
                lastDayInMonthLocal, menuItemId);

        orderItemsInYear.forEach(orderItem -> {
            Integer day = orderItem.getOrder().getDateOfOrder().getDayOfMonth();
            salesPerDays.set(day-1, salesPerDays.get(day-1) + orderItem.getQuantity());
        });

        return salesPerDays;
    }

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
                ratioForMonths.replaceAll(value -> value - Math.round(salary.getValue()/numberDaysInMonth));
            }
        });

        return ratioForMonths;
    }

    @Override
    public List<Integer> preparationTimeForYear(Integer year, Integer employee_id) {
        User user = userService.findById(employee_id);

        if(user == null) {
            throw new MissingEntityException("User with given id does not exist in the system.");
        }

        if(!user.getRoles().get(0).getName().equals("ROLE_COOK") && !user.getRoles().get(0).getName().equals("ROLE_BARTENDER")) {
            throw new BadLogicException("This report can only show data of cooks and bartenders");
        }

        List<Integer> minutesForYear = new ArrayList<Integer>(Collections.nCopies(12, 0));

        LocalDateTime firstDayInYearLocal = getDayInYear(year, 0, 1);
        LocalDateTime lastDayInYearLocal = getDayInYear(year, 11, 31);

        List<OrderItem> orderItems = null;
        if(user.getRoles().get(0).getName().equals("ROLE_COOK")) {
            orderItems = orderItemService.findByCook(employee_id, firstDayInYearLocal,
                    lastDayInYearLocal);
        }
        else {
            orderItems = orderItemService.findByBartender(employee_id, firstDayInYearLocal,
                    lastDayInYearLocal);
        }

        orderItems.forEach(orderItem -> {
            Integer monthValue = orderItem.getOrder().getDateOfOrder().getMonthValue();
            minutesForYear.set(monthValue - 1, minutesForYear.get(monthValue - 1) +
                    orderItem.getMenuItem().getPreparationTime() * orderItem.getQuantity());
        });

        return minutesForYear;
    }

    @Override
    public List<Integer> preparationTimeForMonth(Integer year, Integer month, Integer employee_id) {
        User user = userService.findById(employee_id);

        if(user == null) {
            throw new MissingEntityException("User with given id does not exist in the system.");
        }

        if(!user.getRoles().get(0).getName().equals("ROLE_COOK") && !user.getRoles().get(0).getName().equals("ROLE_BARTENDER")) {
            throw new BadLogicException("This report can only show data of cooks and bartenders");
        }

        Integer numberDaysInMonth = getNumberDaysInMonth(year, month-1);
        List<Integer> minutesForMonth = new ArrayList<Integer>(Collections.nCopies(numberDaysInMonth, 0));

        LocalDateTime firstDayInMonthLocal = getDayInYear(year, month-1, 1);
        LocalDateTime lastDayInMonthLocal = getDayInYear(year, month-1, numberDaysInMonth);

        List<OrderItem> orderItems = null;
        if(user.getRoles().get(0).getName().equals("ROLE_COOK")) {
            orderItems = orderItemService.findByCook(employee_id, firstDayInMonthLocal,
                    lastDayInMonthLocal);
        }
        else {
            orderItems = orderItemService.findByBartender(employee_id, firstDayInMonthLocal,
                    lastDayInMonthLocal);
        }

        orderItems.forEach(orderItem -> {
            Integer dayValue = orderItem.getOrder().getDateOfOrder().getDayOfMonth();
            minutesForMonth.set(dayValue - 1, minutesForMonth.get(dayValue - 1) +
                    orderItem.getMenuItem().getPreparationTime() * orderItem.getQuantity());
        });

        return minutesForMonth;
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
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return calendar.getActualMaximum(Calendar.DATE);
    }
}
