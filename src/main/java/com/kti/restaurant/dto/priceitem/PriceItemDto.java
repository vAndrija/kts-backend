package com.kti.restaurant.dto.priceitem;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class PriceItemDto {
    @NotEmpty(message = "Value should not be null or empty")
    private Double value;

    @NotNull(message = "Start date should not be null or empty")
    private LocalDate startDate;

    @NotNull(message = "End date should not be null or empty")
    private LocalDate endDate;

    @NotEmpty(message = "Menu item id should not be null or empty")
    private Integer menuItemId;

    @NotNull(message = "Is current should not be null or empty")
    private Boolean isCurrent;

    public PriceItemDto(Double value, LocalDate startDate, LocalDate endDate, Integer menuItemId, Boolean isCurrent) {
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
        this.menuItemId = menuItemId;
        this.isCurrent = isCurrent;
    }

    public PriceItemDto() {

    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
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

    public Integer getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Integer menuItemId) {
        this.menuItemId = menuItemId;
    }

    public Boolean getCurrent() {
        return isCurrent;
    }

    public void setCurrent(Boolean current) {
        isCurrent = current;
    }
}
