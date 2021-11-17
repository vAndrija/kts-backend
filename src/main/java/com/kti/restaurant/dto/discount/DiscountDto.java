package com.kti.restaurant.dto.discount;

import com.kti.restaurant.model.MenuItem;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class DiscountDto {
    @NotNull(message = "Value should not be null")
    private Integer value;

    @NotNull(message = "Start date should not be null")
    private LocalDate startDate;

    @NotNull(message = "End date should not be null")
    private LocalDate endDate;

    @NotNull(message = "Menu item id should not be null")
    private Integer menuItemId;

    @NotNull(message = "Is current should not be null")
    private Boolean isCurrent;

    public DiscountDto(Integer value, LocalDate startDate, LocalDate endDate, Integer menuItemId, Boolean isCurrent) {
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
        this.menuItemId = menuItemId;
        this.isCurrent = isCurrent;
    }

    public DiscountDto() {

    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getCurrent() {
        return isCurrent;
    }

    public void setCurrent(Boolean current) {
        isCurrent = current;
    }

    public Integer getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Integer menuItemId) {
        this.menuItemId = menuItemId;
    }
}
