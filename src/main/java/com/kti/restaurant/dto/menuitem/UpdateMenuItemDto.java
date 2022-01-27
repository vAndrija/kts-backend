package com.kti.restaurant.dto.menuitem;

import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.model.enums.MenuItemType;

import javax.validation.constraints.Min;
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
    private String category;

    @NotNull(message = "Menu id should not be null or empty")
    private Integer menuId;

    @NotNull(message = "Accepted should not be null or empty")
    private Boolean accepted;

    @NotNull(message = "Preparation time should not be null")
    @Min(message = "Preparation time should be bigger than 0", value = 1 )
    private Integer preparationTime;

    public UpdateMenuItemDto(String name, String description, MenuItemType type, String category,
                             Integer menu, Boolean accepted, Integer preparationTime) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.category = category;
        this.accepted = accepted;
        this.menuId = menu;
        this.preparationTime = preparationTime;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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

    public Integer getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(Integer preparationTime) {
        this.preparationTime = preparationTime;
    }
}
