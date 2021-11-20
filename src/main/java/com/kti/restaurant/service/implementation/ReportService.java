package com.kti.restaurant.service.implementation;

import com.kti.restaurant.model.OrderItem;
import com.kti.restaurant.model.PriceItem;
import com.kti.restaurant.service.contract.IOrderItemService;
import com.kti.restaurant.service.contract.IPriceItemService;
import com.kti.restaurant.service.contract.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class ReportService implements IReportService {
    private IOrderItemService orderItemService;
    private IPriceItemService priceItemService;

    @Autowired
    public  ReportService(IOrderItemService orderItemService, IPriceItemService priceItemService) {
        this.orderItemService = orderItemService;
        this.priceItemService = priceItemService;
    }

    @Override
    public List<Double> mealDrinkCostsForYear(Integer year) {
        List<Double> costsPerMonths = new ArrayList<Double>(Collections.nCopies(12, 0.0));

        Date firstDayInYear = getDayInYear(year, 0, 1);
        Date lastDayInYear = getDayInYear(year, 11, 31);

        LocalDateTime firstDayInYearLocal = dateToLocalDateTime(firstDayInYear);
        LocalDateTime lastDayInYearLocal = dateToLocalDateTime(lastDayInYear);

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

        Date firstDayInMonth = getDayInYear(year, month-1, 1);
        Date lastDayInMonth = getDayInYear(year, month-1, numberDaysInMonth);

        LocalDateTime firstDayInMonthLocal = dateToLocalDateTime(firstDayInMonth);
        LocalDateTime lastDayInMonthLocal = dateToLocalDateTime(lastDayInMonth);

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

    private Date getDayInYear(Integer year, Integer month, Integer day) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        return calendar.getTime();
    }

    private Integer getNumberDaysInMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return calendar.getActualMaximum(Calendar.DATE);
    }

    private LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
