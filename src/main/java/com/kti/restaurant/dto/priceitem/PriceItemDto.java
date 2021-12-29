package com.kti.restaurant.dto.priceitem;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class PriceItemDto {
    @NotNull(message = "Value should not be null or empty")
    @Min(value = 1, message = "Value should be bigger than 0")
    private Double value;

    @NotNull(message = "Start date should not be null or empty")
    private LocalDate startDate;

    @NotNull(message = "End date should not be null or empty")
    private LocalDate endDate;

    @NotNull(message = "Menu item id should not be null or empty")
    private Integer menuItemId;

    private Boolean isCurrent;

    @NotNull(message = "Preparation value should not be null or empty")
    @Min(value = 1, message = "Preparation value should be bigger than 0")
    private Double preparationValue;

    public PriceItemDto(Double value, LocalDate startDate, LocalDate endDate, Integer menuItemId, Boolean isCurrent, Double preparationValue) {
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
        this.menuItemId = menuItemId;
        this.isCurrent = isCurrent;
        this.preparationValue = preparationValue;
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

    public Double getPreparationValue() {
        return preparationValue;
    }

    public void setPreparationValue(Double preparationValue) {
        this.preparationValue = preparationValue;
    }
}
