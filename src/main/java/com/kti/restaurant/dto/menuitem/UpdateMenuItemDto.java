package com.kti.restaurant.dto.menuitem;

import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.model.enums.MenuItemType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class UpdateMenuItemDto {
    @NotEmpty(message = "Name should not be null or empty")
    private String name;

    @NotEmpty(message = "Description should not be null or empty")
    private String description;

    @NotNull(message = "Type should not be null")
    private MenuItemType type;

    @NotNull(message = "Category should not be null")
    private MenuItemCategory category;

    @NotNull(message = "Menu id should not be null or empty")
    private Integer menuId;

    @NotNull(message = "Accepted should not be null or empty")
    private Boolean accepted;

    public UpdateMenuItemDto(String name, String description, MenuItemType type, MenuItemCategory category,
                             Integer menu, Boolean accepted) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.category = category;
        this.accepted = accepted;
        this.menuId = menu;
    }

    public UpdateMenuItemDto() {

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

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menu) {
        this.menuId = menu;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
