package com.kti.restaurant.dto.menuitem;

import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.model.enums.MenuItemType;

public class CreateMenuItemDto {
    private String name;

    private String description;

    private MenuItemType type;

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
