package com.kti.restaurant.dto.menuitem;

import com.kti.restaurant.dto.menu.MenuDto;
import com.kti.restaurant.dto.priceitem.PriceItemDto;
import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.model.enums.MenuItemType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class MenuItemDto {

    @NotNull(message = "Id should not be null")
    private Integer id;

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

    private MenuDto menuDto;

    private Boolean accepted;

    public MenuItemDto(Integer id, String name, String description, MenuItemType type, String category, int preparationTime,
                       PriceItemDto priceItemDto, MenuDto menuDto, Boolean accepted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.category = category;
        this.preparationTime = preparationTime;
        this.priceItemDto = priceItemDto;
        this.menuDto = menuDto;
        this.accepted = accepted;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MenuDto getMenuDto() {
        return menuDto;
    }

    public void setMenuDto(MenuDto menuDto) {
        this.menuDto = menuDto;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
