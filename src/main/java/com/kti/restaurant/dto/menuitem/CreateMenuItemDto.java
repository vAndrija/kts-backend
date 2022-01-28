package com.kti.restaurant.dto.menuitem;

import com.kti.restaurant.dto.menu.MenuDto;
import com.kti.restaurant.dto.priceitem.PriceItemDto;
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
    private String category;

    @NotNull(message = "Preparation time should not be null")
    private Integer preparationTime;

    private PriceItemDto priceItemDto;

    @NotEmpty(message = "Image name should not be null or empty")
    private String imageName;

    public CreateMenuItemDto(String name, String description, MenuItemType type, String category, int preparationTime,
                             PriceItemDto priceItemDto, String imageName) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.category = category;
        this.preparationTime = preparationTime;
        this.priceItemDto = priceItemDto;
        this.imageName = imageName;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
