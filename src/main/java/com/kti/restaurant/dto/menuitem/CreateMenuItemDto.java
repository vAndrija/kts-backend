package com.kti.restaurant.dto.menuitem;

import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.model.enums.MenuItemType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class CreateMenuItemDto {
    @NotEmpty(message = "Name should not be null or empty")
    private String name;

    @NotEmpty(message = "Description should not be null or empty")
    private String description;

    @NotNull(message = "Type should not be null")
    private MenuItemType type;

    @NotNull(message = "Category should not be null")
    private MenuItemCategory category;

    public CreateMenuItemDto(String name, String description, MenuItemType type, MenuItemCategory category) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.category = category;
    }

    public CreateMenuItemDto() {

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
}