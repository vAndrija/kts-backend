package com.kti.restaurant.dto.menuitem;

import com.kti.restaurant.dto.priceitem.PriceItemDto;
import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.model.enums.MenuItemType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class MenuItemDto {
    @NotEmpty(message = "Name should not be null or empty")
    private String name;

    @NotEmpty(message = "Description should not be null or empty")
    private String description;

    @NotNull(message = "Type should not be null")
    private MenuItemType type;

    @NotNull(message = "Category should not be null")
    private MenuItemCategory category;

    @NotNull(message = "Preparation time should not be null")
    private Integer preparationTime;

    private PriceItemDto priceItemDto;

    public MenuItemDto(String name, String description, MenuItemType type, MenuItemCategory category, int preparationTime,
                       PriceItemDto priceItemDto) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.category = category;
        this.preparationTime = preparationTime;
        this.priceItemDto = priceItemDto;
    }

    public MenuItemDto() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MenuItemType getType() {
        return type;
    }

    public void setType(MenuItemType type) {
        this.type = type;
    }

    public MenuItemCategory getCategory() {
        return category;
    }

    public void setCategory(MenuItemCategory category) {
        this.category = category;
    }

    public Integer getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(Integer preparationTime) {
        this.preparationTime = preparationTime;
    }

    public PriceItemDto getPriceItemDto() {
        return priceItemDto;
    }

    public void setPriceItemDto(PriceItemDto priceItemDto) {
        this.priceItemDto = priceItemDto;
    }
}
